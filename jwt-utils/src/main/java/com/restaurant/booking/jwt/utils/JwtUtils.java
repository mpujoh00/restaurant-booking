package com.restaurant.booking.jwt.utils;

import com.restaurant.booking.user.model.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromJwtToken(String token){
        return (List<String>) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("authorities");
    }

    public boolean isValidJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    public String getAuthorizationHeader() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accessToken = user.getJwtToken();
        return "Bearer " + accessToken;
    }

}
