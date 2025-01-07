package Rest.Gym.RestGym.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/unsecured")
    public String unsecured() {
        return "Unsecured";
    }

    @GetMapping("/secured")
    public String secured() {
        return "secured";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/info")
    public String userInfo(Principal principal) {
        return principal.getName();
    }
}
