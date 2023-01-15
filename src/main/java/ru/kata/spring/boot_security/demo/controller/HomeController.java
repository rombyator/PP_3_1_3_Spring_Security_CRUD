package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class HomeController {
    private final UserDetailsService userDetailsService;

    public HomeController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public String home(Model model, Principal principal) {
        var user = Optional
            .ofNullable(principal)
            .map(Principal::getName)
            .map(userDetailsService::loadUserByUsername)
            .orElse(User.anonymousUser());
        model.addAttribute("user", user);

        return "index";
    }

    @GetMapping("/forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
