package com.example.j2ee_frontend_test.controller;

import com.example.j2ee_frontend_test.models.Action;
import com.example.j2ee_frontend_test.models.Role;
import com.example.j2ee_frontend_test.models.RoleAction;
import com.example.j2ee_frontend_test.response.RoleListResponse;
import com.example.j2ee_frontend_test.services.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/role")
public class RoleController {

    @Autowired
    RoleService roleService;
    @GetMapping("/all")
    public String getAllRoles(Model model) {
        List<Role> roleListResponse=roleService.getAllRoles();
        model.addAttribute("data",roleListResponse);
        return "role";
    }
    @GetMapping("/new")
    public String newRole(Model model) {
        Role r=new Role();
        Role a=roleService.getRoleById(1);
        model.addAttribute("role",r);
        model.addAttribute("action",a);
        return "create_role";
    }
    @PostMapping("/save")
    public String addRole(@ModelAttribute("role") Role role) throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        String charityJson = objectMapper.writeValueAsString(role);
        System.out.println(charityJson);
        return "redirect:/role/all";
    }
    @GetMapping("/{id}")
    public String detailRole(Model model, @PathVariable("id") int id) {
        Role role = roleService.getRoleById(id);
        List<RoleAction> l=role.getRoleActions();
        RoleAction action1 = l.stream()
                .filter(roleAction -> roleAction.getAction().getId() == 1)
                .findFirst() // Lấy phần tử đầu tiên
                .orElse(null);
        RoleAction action2 = l.stream()
                .filter(roleAction -> roleAction.getAction().getId() == 2)
                .findFirst() // Lấy phần tử đầu tiên
                .orElse(null);
        RoleAction action3 = l.stream()
                .filter(roleAction -> roleAction.getAction().getId() == 3)
                .findFirst() // Lấy phần tử đầu tiên
                .orElse(null);
        RoleAction action4 = l.stream()
                .filter(roleAction -> roleAction.getAction().getId() == 4)
                .findFirst() // Lấy phần tử đầu tiên
                .orElse(null);
        System.out.println(l);
        System.out.println(action1);
        System.out.println(action2);
        System.out.println(action3);
        System.out.println(action4);
        model.addAttribute("data", role);
        model.addAttribute("action1", action1);
        model.addAttribute("action2", action2);
        model.addAttribute("action3", action3);
        model.addAttribute("action4", action4);
        return "detail_role";
    }
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") int id,Model model){
        String c=roleService.deleteRole(id);
        model.addAttribute("msg",c);
        return "redirect:/role/all";

    }

}
