package com.app.security.jwt;

import com.app.config.LoggerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.app.utils.Utils.tagMethodName;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TAG = "JwtAuthenticationFilter";
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final LoggerService loggerService;

    public JwtAuthenticationFilter(HandlerExceptionResolver handlerExceptionResolver, JwtUtil jwtUtil,
                                   LoggerService loggerService, UserDetailsService userDetailsService) {
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.userDetailsService = userDetailsService;
        this.loggerService = loggerService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String methodName = "shouldNotFilter";
//        String path = request.getRequestURI();
        String path = request.getServletPath();
        boolean shouldNotFilter = path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/api/v1/auth")
                || path.startsWith("/api/auth")
                || path.startsWith("/api/v1/test")
                || path.startsWith("/api/test")
                || path.startsWith("/actuator");
        loggerService.info(tagMethodName(TAG, methodName), " Request path: " + path + ", shouldNotFilter: " + shouldNotFilter);
        return shouldNotFilter;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String methodName = "doFilterInternal";
        loggerService.info(tagMethodName(TAG, methodName), "Processing request: " + request.getRequestURI());

        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response); // allow request to proceed
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            loggerService.warn(tagMethodName(TAG, methodName), "No valid Authorization header found");
            sendErrorResponse(response, "No valid Authorization header found");
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String username = jwtUtil.extractUsername(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (username != null && authentication == null) {
                loggerService.info(tagMethodName(TAG, methodName), "Valid token found for user: " + username);

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                loggerService.info(tagMethodName(TAG, methodName), "User details: " + userDetails);

                // Validate the token before setting authentication
                if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    loggerService.info(tagMethodName(TAG, methodName), "User authenticated successfully: " + username);
                } else {
                    loggerService.warn(tagMethodName(TAG, methodName), "Token is invalid for user: " + username);
                    sendErrorResponse(response, "Invalid token. Please log in again.");
                    return;
                }
            }

            // Proceed with the filter chain *after authentication is set*
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            loggerService.warn(tagMethodName(TAG, methodName), "Token expired: " + ex.getMessage());
            sendErrorResponse(response, "Token has expired. Please log in again.");
        } catch (SignatureException | MalformedJwtException ex) {
            loggerService.error(tagMethodName(TAG, methodName), "Invalid JWT", ex);
            sendErrorResponse(response, "Invalid JWT. Please check your token.");
        } catch (Exception exception) {
            loggerService.error(tagMethodName(TAG, methodName), "Unable to filter", exception);
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }


    /**
     * Helper method to send structured error response.
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // Creating response in the expected format
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", new Date());
        errorResponse.put("status_code", HttpStatus.UNAUTHORIZED.value());
        errorResponse.put("success", false);
        errorResponse.put("data", null);
        errorResponse.put("message", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
