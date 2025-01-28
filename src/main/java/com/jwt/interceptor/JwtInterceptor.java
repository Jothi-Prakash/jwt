package com.jwt.interceptor;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jwt.DTO.RequestMeta;
import com.jwt.utils.JwtUtils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final RequestMeta requestMeta;

    @Autowired
    public JwtInterceptor(JwtUtils jwtUtils, RequestMeta requestMeta) {
        this.jwtUtils = jwtUtils;
        this.requestMeta = requestMeta;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       
    	System.out.println("---Pre Handle Is Calling---");
    	
    	String auth = request.getHeader("authorization");

        if (!request.getRequestURI().contains("/user/login") && 
            !request.getRequestURI().contains("/user/signup") && 
            !request.getRequestURI().contains("/user/refresh")) {

            try {
                Claims claims = jwtUtils.verify(auth);

                if (claims == null) {
                    throw new AccessDeniedException("Invalid JWT signature.");
                }

                // Set user information in RequestMeta
                requestMeta.setName(claims.get("name").toString());
                requestMeta.setEmailId(claims.get("emailId").toString());

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"code\": \"401\", \"message\": \"Unauthorized: Invalid JWT signature.\"}");
                response.getWriter().flush();
                return false;  
            }
        }

        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
       
    	System.out.println("---Post Handle Is calling---");
    	
        if (response.getStatus() == HttpServletResponse.SC_OK) {
            System.out.println("Request URI: " + request.getRequestURI());
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Request processed successfully.");
        } else {
            System.out.println("Error while processing request: " + response.getStatus());
        }
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    		throws Exception {
    	
    	System.out.println("Request to " + request.getRequestURI() + " completed.");

    }
    
    


}
