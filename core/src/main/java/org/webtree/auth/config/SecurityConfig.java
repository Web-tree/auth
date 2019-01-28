package org.webtree.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.webtree.auth.security.JwtAuthenticationEntryPoint;
import org.webtree.auth.security.JwtAuthenticationTokenFilter;
import org.webtree.auth.service.AuthenticationService;
import org.webtree.auth.service.JwtTokenService;

@ComponentScan("org.webtree.auth")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationService userService;
    private final JwtTokenService tokenUtil;
    private final JwtAuthenticationEntryPoint unauthorizedHandler;
    private AuthConfigurationProperties properties;


    @Autowired
    public SecurityConfig(AuthenticationService userService,
                          JwtTokenService tokenUtil,
                          JwtAuthenticationEntryPoint unauthorizedHandler,
                          AuthConfigurationProperties properties) {
        this.userService = userService;
        this.tokenUtil = tokenUtil;
        this.unauthorizedHandler = unauthorizedHandler;
        this.properties = properties;
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder,
                                        PasswordEncoder passwordEncoder)
            throws Exception {

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

                .antMatchers(
                        properties.getRoute().getLogin(),
                        properties.getRoute().getRegister(),
                        properties.getRoute().getSocialLogin()
                ).permitAll()
                .anyRequest().authenticated();

        // Custom JWT based security filter
        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}