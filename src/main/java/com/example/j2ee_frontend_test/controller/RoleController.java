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

import java.util.*;
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
        // Tạo danh sách các hành động mặc định
        List<Action> defaultActions = Arrays.asList(
                new Action(1, "Account Management", null),
                new Action(2, "Charity Event Management", null),
                new Action(3, "Post Management", null),
                new Action(4, "Accounting", null)
        );


        Role role = new Role();

        role.setRoleActions(new ArrayList<>());
        List<RoleAction> completeRoleActions = new ArrayList<>();
        for (Action action : defaultActions) {
            RoleAction newRoleAction = new RoleAction(
                    null,
                    false,
                    false,
                    false,
                    false,
                    role,
                    action
            );
            completeRoleActions.add(newRoleAction);
        }
        role.setRoleActions(completeRoleActions);
        model.addAttribute("data", role);

        return "create_role";
    }

    @PostMapping("/save")
    public String addRole(@ModelAttribute("role") Role role,Model model){
       if(role.getId() != null) {
           role.setRoleActions(
                   role.getRoleActions().stream()
                           .filter(roleAction -> roleAction.getId() != 0)
                           .collect(Collectors.toList())
           );
           System.out.println("Save: ");
           System.out.println("Role: " + role);
           if (role.getRoleActions() != null) {
               for (RoleAction action : role.getRoleActions()) {
                   System.out.println("RoleAction: " + action);
               }
           } else {
               System.out.println("RoleActions is null");
           }

           String c = roleService.updateRole(role.getId(), role);
           model.addAttribute("msg", c);
           return "redirect:/role/all";
       } else {
//           List<Role> roleListResponse=roleService.getAllRoles();
//           List<Integer> existingIds = roleListResponse.stream()
//                   .map(Role::getId)
//                   .filter(Objects::nonNull)
//                   .sorted()
//                   .collect(Collectors.toList());
//           int newId = 1;
//           for (int i = 0; i < existingIds.size(); i++) {
//               if (existingIds.get(i) != newId) {
//                   break;
//               }
//               newId++;
//           }
//           role.setId(newId);
           System.out.println("Create: ");
           System.out.println("Role: " + role);
           String c = roleService.createRole(role);
           model.addAttribute("msg", c);
           return "redirect:/role/all";
       }
    }
@GetMapping("/{id}")
public String getRoleDetails(Model model ,@PathVariable("id") int id) {
    List<Action> defaultActions = Arrays.asList(
            new Action(1, "Account Management",null),
            new Action(2, "Charity Event Management",null),
            new Action(3, "Post Management",null),
            new Action(4, "Accounting",null)
    );
    Role role=roleService.getRoleById(id);
    System.out.println("Detail: " );
    System.out.println("Role: " + role);
    for (RoleAction action : role.getRoleActions()) {
        System.out.println("RoleAction: " + action);
    }
    List<RoleAction> completeRoleActions = new ArrayList<>();
    for (Action action : defaultActions) {
        RoleAction matchingRoleAction = role.getRoleActions().stream()
                .filter(ra -> ra.getAction().getId() == action.getId())
                .findFirst()
                .orElse(null);
        if (matchingRoleAction == null) {
            matchingRoleAction = new RoleAction(0, false, false, false, false,role,action);
        }

        completeRoleActions.add(matchingRoleAction);
    }

    role.setRoleActions(completeRoleActions);
    model.addAttribute("data", role);
    return "detail_role";
}
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") int id,Model model){
        if (id >3) {
            String c = roleService.deleteRole(id);
            model.addAttribute("msg", c);
            return "redirect:/role/all";
        }
        else {
            String c="không thể xóa role gốc";
            model.addAttribute("msg", c);
            return "redirect:/role/all";
        }
    }

}
