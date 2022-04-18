package com.zoomers.GameSetMatch.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class FireBaseTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        System.out.println("request: " + request.getRequestURI());
        String authenticationHeader = request.getHeader("Authorization");
        System.out.println("Header is " +  authenticationHeader);
        //checks if token is there
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer "))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Missing token!");
        System.out.println("Passed Missing Token Check");
        FirebaseToken decodedToken = null;
        try {
            //Extracts token from header
            String token = authenticationHeader.substring(7, authenticationHeader.length());
            //verifies token to firebase server
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
            if(decodedToken != null){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(decodedToken.getUid(), decodedToken, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            System.out.println("Token has been decoded" + decodedToken);

        }
        catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Error! "+e.toString());
        }

        //if token is invalid
        if (decodedToken==null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid token!");
        }

        chain.doFilter(request,response);
    }
}

