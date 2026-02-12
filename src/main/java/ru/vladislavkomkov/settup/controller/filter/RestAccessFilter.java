package ru.vladislavkomkov.settup.controller.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.rmi.AccessException;

public class RestAccessFilter implements Filter {
    @Value("spring.application.is-active-rest")
    private boolean isActiveRest;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        if (path.startsWith("/rest/") && !isActiveRest) {
            throw new AccessException("Not allowed");
        }

        chain.doFilter(request, response);
    }
}
