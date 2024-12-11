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
        destroyHeaderAuthority();
        authorities = null;
        httpSession.removeAttribute("username");
        httpSession.removeAttribute("rememberMe");
    }

    public void setToken(String token, boolean rememberMe) {
        httpSession.setAttribute("jwtToken", token);
        httpSession.setAttribute("username", getUsernameFromToken(token));
        getAuthoritiesFromToken(token);
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

    public boolean containAuthority(String authority) {
        return authorities.stream().anyMatch(a -> a.getAuthority().equals(authority));
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        if (authorities == null || authorities.isEmpty()) {
            System.out.println("getAuthoritiesFromToken: " + token);
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            authorities = (((List<?>) claims.get("authorities")).stream()
                    .map(auth -> new SimpleGrantedAuthority((String) auth))
                    .collect(Collectors.toList()));
            for (GrantedAuthority grantedAuthority : authorities) {
                System.out.println(grantedAuthority);
            }
            setHeaderAuthority();
        }
        return authorities;
    }

    public void setHeaderAuthority() {
        httpSession.setAttribute("account_management", containAuthority("Account Management read"));
        httpSession.setAttribute("role_management", containAuthority("Role Management read"));
        httpSession.setAttribute("charity_event_management", containAuthority("Charity Event Management read"));
        httpSession.setAttribute("post_management", containAuthority("Post Management read"));
        httpSession.setAttribute("accounting", containAuthority("Accounting read"));
        System.out.println("setHeaderAuthority: " + containAuthority("Accounting"));
        System.out.println("setHeaderAuthority: " + httpSession.getAttribute("Accounting"));
    }

    public void destroyHeaderAuthority() {
        httpSession.removeAttribute("account_management");
        httpSession.removeAttribute("role_management");
        httpSession.removeAttribute("charity_event_management");
        httpSession.removeAttribute("post_management");
        httpSession.removeAttribute("accounting");
    }


}
