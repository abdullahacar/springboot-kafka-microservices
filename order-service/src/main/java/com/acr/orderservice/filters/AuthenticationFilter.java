package com.acr.orderservice.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final String[] excludedEndpoints = new String[] {"*/swagger/**, */bar/**"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("Once per request filter is handled !");
        System.out.println("Auth Type : " + request.getAuthType());
        System.out.println("Request URI : " + request.getRequestURI());
        System.out.println("Remote Host : " + request.getRemoteHost());
        System.out.println("Remote Address : " + request.getRemoteAddr());
        System.out.println("Remote Port : " + request.getRemotePort());
        System.out.println("Remote User : " + request.getRemoteUser());
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }


}
