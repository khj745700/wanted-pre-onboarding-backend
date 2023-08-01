package com.wanted.preonboarding.backend.security.configs;



import com.wanted.preonboarding.backend.security.filter.JWTAuthenticationFilter;
import com.wanted.preonboarding.backend.security.filter.JsonLoginProcessingFilter;
import com.wanted.preonboarding.backend.security.handler.CustomAccessDeniedHandler;
import com.wanted.preonboarding.backend.security.handler.CustomLoginAuthEntryPoint;
import com.wanted.preonboarding.backend.security.handler.JsonAuthFailureHandler;
import com.wanted.preonboarding.backend.security.handler.JsonAuthSuccessHandler;
import com.wanted.preonboarding.backend.security.provider.JWTAuthenticationProvider;
import com.wanted.preonboarding.backend.security.provider.JsonAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
@Slf4j
@EnableWebSecurity
public class SecurityConfig{

    private final CorsConfig corsConfig;
    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new JsonAuthSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(){
        return new JsonAuthFailureHandler();
    }

    @Bean
    public AuthenticationEntryPoint jsonLoginAuthEntryPoint() {
        return new CustomLoginAuthEntryPoint();
    }

    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


    @Bean
    public AuthenticationProvider jsonAuthenticationProvider(){
        JsonAuthenticationProvider jsonAuthenticationProvider = new JsonAuthenticationProvider();
        jsonAuthenticationProvider.setbCryptPasswordEncoder(bCryptPasswordEncoder());
        jsonAuthenticationProvider.setUserDetailsService(userDetailsService);
        return jsonAuthenticationProvider;
    }

    @Bean
    public AuthenticationProvider JWTAuthenticationProvider(){
        JWTAuthenticationProvider jwtAuthenticationProvider = new JWTAuthenticationProvider();
        jwtAuthenticationProvider.setUserDetailsService(userDetailsService);
        return jwtAuthenticationProvider;
    }

    public JsonLoginProcessingFilter jsonLoginProcessingFilter(AuthenticationManager authenticationManager) throws Exception {
        JsonLoginProcessingFilter jsonLoginProcessingFilter = new JsonLoginProcessingFilter();

        jsonLoginProcessingFilter.setAuthenticationManager(authenticationManager);

        // Auth Success/Fail 핸들러 설정
        jsonLoginProcessingFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        jsonLoginProcessingFilter.setAuthenticationFailureHandler(authenticationFailureHandler());

        return jsonLoginProcessingFilter;
    }



    public JWTAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        return new JWTAuthenticationFilter(authenticationManager);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        AuthenticationManager authenticationManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
        ProviderManager p = (ProviderManager) authenticationManager;
        p.getProviders().add(jsonAuthenticationProvider()); // 계속 Provider에 DaoProvider만 저장되는 현상. 그래서 직접 할당하게 됨.
        p.getProviders().add(JWTAuthenticationProvider());

        return http
                .formLogin().disable()
                .httpBasic().disable()
                .logout().disable()
                .csrf().disable()
                // `api/` 이하 URL에 한해서 설정 클래스 동작
                .authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .anyRequest().authenticated()
                .and()
                .addFilterAt(jsonLoginProcessingFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(authenticationManager), JsonLoginProcessingFilter.class)

                /* 미인증, 인가예외 핸들러 등록 */
                .exceptionHandling()
                .accessDeniedHandler(jsonAccessDeniedHandler())
                .authenticationEntryPoint(jsonLoginAuthEntryPoint())
                .and()
                .addFilter(corsConfig.corsFilter()) // Enable CORS and disable CSRF //TODO: 왜 CORS는 활성화 하라는 것 ?
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().build(); // Set session management to stateless


    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        return new CorsFilter(source);
//    }


}
