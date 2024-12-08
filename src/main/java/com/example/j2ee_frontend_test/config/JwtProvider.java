package com.example.j2ee_frontend_test.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    private final HttpSession httpSession;

    private List<GrantedAuthority> authorities;

    public JwtProvider(HttpSession httpSession) {
        this.httpSession = httpSession;
    }

    public String getToken() {
        System.out.println(httpSession.getAttribute("jwtToken"));
        return (String) httpSession.getAttribute("jwtToken");
    }

    public void destroyToken() {
        httpSession.removeAttribute("jwtToken");
        if (httpSession.getAttribute("rememberMe").equals("false")) {
            httpSession.removeAttribute("username");
            httpSession.removeAttribute("rememberMe");
        }
    }

    public void setToken(String token, boolean rememberMe) {
        httpSession.setAttribute("jwtToken", token);
        httpSession.setAttribute("username", getUsernameFromToken(token));
        if (rememberMe) httpSession.setAttribute("rememberMe", true);
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Get username from JWT token
    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        if (authorities == null) {
            System.out.println("getAuthoritiesFromToken: " + token);
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            authorities = (((List<?>) claims.get("authorities")).stream()
                    .map(auth -> new SimpleGrantedAuthority((String) auth))
                    .collect(Collectors.toList()));
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println(grantedAuthority);
            }
        }
        return authorities;
    }
}
