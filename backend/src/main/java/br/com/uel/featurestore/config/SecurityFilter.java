package br.com.uel.featurestore.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.uel.featurestore.service.TokenService;

import java.io.IOException;
import java.util.Collections;

import java.util.Enumeration;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("doFIlterInternal before recoveryToken - SecurityFilter: " + request);
        String token = recoveryToken(request);
        System.out.println("doFIlterInternal after recoveryToken - SecurityFilter: " + token);
        if (token != null) {
            try {
                String email = tokenService.validateToken(token);
                if (!email.isEmpty()) {
                    var authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                throw new Error("FALHA AO VALIDAR TOKEN: " + e.getMessage());
            }
        } else {
            System.out.println("Nenhum token encontrado na requisição.");
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        System.out.println("TENTANDO ACESSAR: " + request.getMethod() + " " + request.getRequestURI());
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }


        String authHeader = request.getHeader("Authorization");
        System.out.println("recoveryToken - SecurityFilter: " + authHeader);
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}
