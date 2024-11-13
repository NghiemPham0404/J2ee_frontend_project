package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Profile;
import com.example.j2ee_frontend_test.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String showProfile(Model model) {
        Profile profile = profileService.findById(1L);
        model.addAttribute("data", profile);
        return "profile";
    }

//    @PostMapping
//    public String updateProfile( @ModelAttribute("profile") Profile profile, BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            return "profile";
//        }
//
//        try {
//            profileService.updateProfile(profile);
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Không thể cập nhật thông tin!");
//            return "profile";
//        }
//
//        return "redirect:/profile";
//    }
}
