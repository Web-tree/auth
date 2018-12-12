package org.webtree.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.webtree.auth.security.JwtAuthenticationEntryPoint;
import org.webtree.auth.security.JwtAuthenticationTokenFilter;
import org.webtree.auth.service.JwtTokenService;
import org.webtree.auth.service.UserAuthenticationService;


@ComponentScan("org.webtree.auth")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserAuthenticationService userService;
    private final JwtTokenService tokenUtil;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    public SecurityConfig(@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserAuthenticationService userService,
                          JwtTokenService tokenUtil,
                          JwtAuthenticationEntryPoint unauthorizedHandler) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder,
                                        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserAuthenticationService userService,
                                        PasswordEncoder passwordEncoder) throws Exception {

        authenticationManagerBuilder
                .userDetailsService(this.userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter(userService, tokenUtil);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // we don't need CSRF because our token is invulnerable
                .csrf().disable()
                .cors().and()

                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .antMatchers("/rest/token/new", "/rest/user/register", "/rest/social/login").permitAll()
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}
