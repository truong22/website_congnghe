package com.devteria.hello_spring_boot.Security.jwt;

import com.devteria.hello_spring_boot.Security.User.shopUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.io.Decoders;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
public class jwtUtils {

    @Value("${auth.token.jwtSecret}")
    private  String jwtSecret;
    @Value("${auth.token.expirationInMils")
    private int exporationTime;

    public String generateTokenForUser(Authentication authentication){
        shopUserDetails userPrincipal=(shopUserDetails)
                authentication.getPrincipal();
                //tu dong sinh ra dai dien cho nguoi dung chua thong tin user
        List<String> roles=userPrincipal.getAuthorities()
        //danh sach quyen ng dung so huu
        .stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id",userPrincipal.getId())
                .claim("role",roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+exporationTime))
                .signWith(key(), SignatureAlgorithm.ES256).compact();
        }
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFormToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException| UnsupportedJwtException
        | MalformedJwtException| SignatureException|IllegalArgumentException e){
            throw new JwtException(e.getMessage());
        }
    }



}
