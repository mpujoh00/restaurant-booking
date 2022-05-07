package com.restaurant.booking.apigateway;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    public Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public boolean isInvalid(String token){

        if(isExpired(token))
            return true;

        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return false;
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: {}", e.getMessage());
        }
        return true;
    }

    private boolean isExpired(String token){
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }


}
