package org.webtree.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webtree.auth.domain.User;
import org.webtree.auth.exception.AuthenticationException;
import org.webtree.auth.time.TimeProvider;
import org.webtree.auth.util.KeyUtil;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtTokenService {
    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenService.class);
    private static final String PKCS_12 = "PKCS12";
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.RS256;

    @Value("#{AuthPropertiesBean.jwt.secret}")
    private String secret;
    @Value("#{AuthPropertiesBean.jwt.privateKey}")
    private String privateKey;
    @Value("#{AuthPropertiesBean.jwt.publicKey}")
    private String publicKey;
    @Value("#{AuthPropertiesBean.jwt.expiration}")
    private Long expiration;
    private KeyPair keyPair;

    private TimeProvider timeProvider;

    @Autowired
    public JwtTokenService(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
        this.keyPair = new KeyPair(KeyUtil.getPublicKey(publicKey.getBytes(), PKCS_12),
                KeyUtil.getPrivateKey(privateKey.getBytes(), PKCS_12));
    }

    public String getUsernameFromToken(String token) {
        try {
            String username = getClaimFromToken(token, Claims::getSubject);

            if (username == null) {
                throw new AuthenticationException("Unexpected username");
            }

            return username;
        } catch (JwtException e) {
            throw new AuthenticationException("Can't parse token", e);
        }
    }

    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /*public String getAudienceFromToken(String token) {
        return getClaimFromToken(token, Claims::getAudience);
    }*/

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    void setSecret(String secret) {
        this.secret = secret;
    }

    void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(timeProvider.now());
    }

    private boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, user.getUsername(), user.getId());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject, String id) {
        final Date createdDate = timeProvider.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        LOG.info("doGenerateToken {}", createdDate);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setId(id)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(ALGORITHM, keyPair.getPrivate())
                .compact();
    }

    public boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = timeProvider.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(ALGORITHM, keyPair.getPrivate())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (MalformedJwtException exception) {
            LOG.warn("Token can't be parsed. {}", token, exception);
            return false;
        }
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }

    public static class InvalidTokenException extends RuntimeException {
    }
}
