package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.DTOs.JwtDTO;
import com.example.j2ee_frontend_test.DTOs.LoginDTO;
import com.example.j2ee_frontend_test.config.JwtProvider;
import com.example.j2ee_frontend_test.config.RetrofitClientConfig;
import com.example.j2ee_frontend_test.models.Account;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.response.AccountListResponse;
import com.example.j2ee_frontend_test.response.LoginResponse;
import com.example.j2ee_frontend_test.services.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private RetrofitClientConfig retrofitClientConfig;

    @GetMapping("/login")
    public String login(@ModelAttribute(name = "loginDTO") LoginDTO loginDTO) {
        if (jwtProvider.getToken() == null) {
            return "login";
        }
        retrofitClientConfig.reinitializeRetrofit();
        return "redirect:/";
    }

    @PostMapping("/validate-login")
    public String login(Model model, @ModelAttribute(name = "loginDTO") LoginDTO loginDTO, Errors errors, HttpServletResponse httpServletResponse) {
        if (errors.hasErrors()) {
            model.addAttribute("message", "Vui lòng sửa các lỗi sau");
            return "login";
        }
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        boolean rememberMe = loginDTO.isRememberMe();
        LoginResponse loginResponse = loginService.Login(username, password);
        if (loginResponse.getData() != null) {
            JwtDTO jwtDTO = loginResponse.getData();
            jwtProvider.setToken(jwtDTO.getKey(), rememberMe);

            if (loginDTO.isRememberMe()) {
                Cookie cookie2 = new Cookie("rememberMe", loginDTO.isRememberMe() + "");
                cookie2.setMaxAge(Integer.MAX_VALUE);
                cookie2.setHttpOnly(true);
                cookie2.setPath("/");
                cookie2.setSecure(true); // Use true if running on HTTPS
                httpServletResponse.addCookie(cookie2);
            }

            retrofitClientConfig.reinitializeRetrofit();
            System.out.println(jwtDTO.getKey());
            return "redirect:/";
        } else {
            model.addAttribute("message", loginResponse.getError().getError());
            return "login";
        }
    }

    @GetMapping("/forgot-pass")
    public String forgotPassword(@ModelAttribute(name = "loginDTO") LoginDTO loginDTO) {
        return "";
    }

    @GetMapping("/l0g0ut")
    public String logout() {
        jwtProvider.destroyToken();
        return "redirect:/login";
    }
}
