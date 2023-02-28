package com.yxj.ssoserver.security.config;

import com.yxj.ssoserver.security.handler.CustomAccessDeniedHandler;
import com.yxj.ssoserver.security.handler.CustomLogoutHandler;
import com.yxj.ssoserver.security.token.RedisSecurityContextRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import javax.annotation.Resource;
import java.util.List;

@Configuration
public class SpringSecurityConfig {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Resource
    private SecurityContextRepository securityContextRepository;

    @Resource
    private CustomLogoutHandler customLogoutHandler;

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf().disable()
                .formLogin().loginPage("/sso/loginPage.html")
                .loginProcessingUrl("/login")
                .successForwardUrl("/sso/doLogin")
                .and()
                .authorizeHttpRequests()
                .antMatchers("/sso/loginPage.html").permitAll()
                .antMatchers("/test/a").hasRole("user")
                .antMatchers("/static/*.ico").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .and()
                .logout((logoutConfigurer)->{
                    List<LogoutHandler> logoutHandlers = logoutConfigurer.getLogoutHandlers();
                    logoutHandlers.add(0,customLogoutHandler);
                })
                .securityContext((securityContext)->{
                    //这里可设置自定义的认证存储地方
                    securityContext.securityContextRepository(securityContextRepository);
                })
                .userDetailsService(userDetailsService);

        return httpSecurity.build();

    }

    @Bean
    public CorsFilter corsFilter() {

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().antMatchers("/static/css/*.css",
                "/static/js/*.js","/static/*.ico","/resources/**");

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        return authenticationConfiguration.getAuthenticationManager();
    }

}
