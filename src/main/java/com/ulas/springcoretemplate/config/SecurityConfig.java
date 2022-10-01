package com.ulas.springcoretemplate.config;


import com.ulas.springcoretemplate.auth.JwtConfig;
import com.ulas.springcoretemplate.auth.UserDetailsServiceImp;
import com.ulas.springcoretemplate.constant.ApprovedConstants;
import com.ulas.springcoretemplate.core.LoggingBean;
import com.ulas.springcoretemplate.enums.Role;
import com.ulas.springcoretemplate.filter.JwtTokenVerifier;
import com.ulas.springcoretemplate.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.ulas.springcoretemplate.interfaces.repository.user.UserRepository;
import com.ulas.springcoretemplate.interfaces.service.home.LoginService;
import com.ulas.springcoretemplate.interfaces.service.home.RegisterService;
import com.ulas.springcoretemplate.interfaces.service.other.JwtService;
import com.ulas.springcoretemplate.redis.RedisTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImp userDetailsServiceImp;
    private final JwtConfig jwtConfig;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RedisTokenManager redisTokenManager;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final LoggingBean LOG;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Value("${spring.profiles.active}")
    private String profile;


    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.cors()
                .and()
                .csrf().disable()// TODO: Enabeble this in production
                .formLogin()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(3)
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
                .and()
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig,
                        redisTokenManager, registerService, loginService, jwtService))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, jwtService, redisTokenManager, userRepository, LOG),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/home/**", "/swagger-ui/**", "/swagger-ui/#", "login").permitAll()
                .antMatchers("/user/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/admin/**").hasAnyRole(Role.ADMIN.name())
        ;
    }

    @Bean
    public SpringSessionBackedSessionRegistry<? extends Session> sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(ApprovedConstants.PERMIT_URLS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsServiceImp);
        return provider;
    }
}
