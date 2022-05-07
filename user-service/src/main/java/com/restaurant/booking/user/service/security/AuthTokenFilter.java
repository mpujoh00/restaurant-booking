package com.restaurant.booking.user.service.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public AuthTokenFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        try {
//            String jwtToken = parseJwt(request);
//            if(jwtToken != null && jwtUtils.validateJwtToken(jwtToken)){
//                String email = jwtUtils.getUsernameFromJwtToken(jwtToken);
//
//                List<GrantedAuthority> roles = jwtUtils.getRolesFromJwtToken(jwtToken).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//
//                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, roles);
//                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//            }
//
//        } catch (Exception ex){
//            log.info("Cannot set user authentication: ", ex);
//        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request){
//        String headerAuth = request.getHeader("Authorization");
//
//        if(StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")){
//            return headerAuth.substring(7);
//        }
        return null;
    }
}
