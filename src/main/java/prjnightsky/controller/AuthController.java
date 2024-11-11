package prjnightsky.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import prjnightsky.entity.Role;
import prjnightsky.entity.User;
import prjnightsky.service.UserService;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String email,
                               @RequestParam String password,
                               Model model) {
        try {
            if (userService.existsByUsername(username)) {
                model.addAttribute("error", "Username already exists");
                return "register";
            }

            if (userService.findByEmail(email).isPresent()) {
                model.addAttribute("error", "Email already exists");
                return "register";
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.USER);

            userService.save(user);
            return "redirect:/auth/login?registered=true";

        } catch (Exception e) {
            model.addAttribute("error", "An error occurred during registration");
            return "register";
        }
    }
}