package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.exceptions.UserEmailAlreadyInUse;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAll());

        return "admin/index";
    }

    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getById(id));

        return "admin/show";
    }

    @GetMapping("/add")
    public String addUser(Model model, @ModelAttribute("newUser") User newUser) {
        model.addAttribute("roles", roleService.getAll());
        model.addAttribute("newUser", newUser == null ? new User() : newUser);

        return "admin/add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user, RedirectAttributes attributes) {
        try {
            userService.add(user);
        } catch (UserEmailAlreadyInUse e) {
            attributes.addFlashAttribute("newUser", user);
            return "redirect:/admin/add?emailInUse";
        }

        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        var user = userService.getById(id);
        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAll());

        return "admin/edit";
    }

    @PostMapping("/{id}/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);

        return "redirect:/admin";
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.delete(id);

        return "redirect:/admin";
    }
}
