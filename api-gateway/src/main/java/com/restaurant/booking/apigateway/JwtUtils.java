package com.restaurant.booking.apigateway;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;

//@Slf4j
@Component
public class JwtUtils {

//    @Value("${jwt.jwtSecret}")
//    private String jwtSecret;

    private Key key;

    @PostConstruct
    public void init(){
        //this.key = Keys.hmacShakeyFor(jwtSecret.getBytes());
    }

}
