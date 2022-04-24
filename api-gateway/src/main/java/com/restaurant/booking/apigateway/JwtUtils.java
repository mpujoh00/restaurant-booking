package com.restaurant.booking.apigateway;

import org.springframework.stereotype.Component;

//@Slf4j
@Component
public class JwtUtils {

//    @Value("${jwt.jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${jwt.jwtExpirationMs}")
//    private int jwtExpirationMs;
//
//    public String generateJwtToken(Authentication authentication){
//
//        User userPrincipal = (User) authentication.getPrincipal();
//
//        return Jwts.builder()
//                .setSubject(userPrincipal.getUsername())
//                .claim("authorities", userPrincipal.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//
//    public String getUsernameFromJwtToken(String token){
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    @SuppressWarnings("unchecked")
//    public List<String> getRolesFromJwtToken(String token){
//        return (List<String>) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("authorities");
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException e) {
//            log.info("Invalid JWT signature: {}", e.getMessage());
//        } catch (MalformedJwtException e) {
//            log.info("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            log.info("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            log.info("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty: {}", e.getMessage());
//        }
//        return false;
//    }
}
