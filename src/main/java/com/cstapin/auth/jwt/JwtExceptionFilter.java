package com.cstapin.auth.jwt;

import com.cstapin.exception.ExceptionResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e);
        } catch (JwtException e) {
            setErrorResponse(HttpStatus.FORBIDDEN, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ExceptionResponse jwtExceptionResponse = new ExceptionResponse(status.name(), e.getMessage());
        response.getWriter().write(jwtExceptionResponse.convertToJson());
    }
}
