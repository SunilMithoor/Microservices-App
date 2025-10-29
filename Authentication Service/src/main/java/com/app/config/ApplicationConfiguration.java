package com.app.config;


import com.app.service.UserDetailsLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.app.utils.Utils.tagMethodName;


@Configuration
@EnableWebSecurity
public class ApplicationConfiguration {

    private static final String TAG = "ApplicationConfiguration";
    private final LoggerService logger;

    public ApplicationConfiguration(LoggerService logger) {
        this.logger = logger;
    }

    @Bean
    UserDetailsService userDetailsService(UserDetailsLoader loader) {
        return loader::loadUserByLoginId;
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        logger.info(tagMethodName(TAG, "byteArrayHttpMessageConverter"), "Initializing ByteArrayHttpMessageConverter");
        return new ByteArrayHttpMessageConverter();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        logger.info(tagMethodName(TAG, "passwordEncoder"), "Initializing BCryptPasswordEncoder");
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        String methodName = "authenticationManager";
        logger.info(tagMethodName(TAG, methodName), "Initializing AuthenticationManager");
        return config.getAuthenticationManager();
    }


    // ✅ Register custom AuthenticationProvider manually
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        String methodName = "authenticationProvider";
        logger.info(tagMethodName(TAG, methodName), "Initializing AuthenticationProvider");

        return new AuthenticationProvider() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                var userDetails = userDetailsService.loadUserByUsername(username);
                if (!passwordEncoder().matches(password, userDetails.getPassword())) {
                    throw new BadCredentialsException("Invalid credentials");
                }
                return new UsernamePasswordAuthenticationToken(
                        userDetails, password, userDetails.getAuthorities());
            }

            @Override
            public boolean supports(Class<?> authentication) {
                return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
            }
        };
    }

}
