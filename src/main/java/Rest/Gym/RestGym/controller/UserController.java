package Rest.Gym.RestGym.controller;

import Rest.Gym.RestGym.dto.UserDto;
import Rest.Gym.RestGym.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = userService.findByRole("USER");
        return ResponseEntity.ok(users);
    }

    @GetMapping("/getAllAdmins")
    public ResponseEntity<List<UserDto>> getAllAdmins(){
        List<UserDto> admin = userService.findByRole("ADMIN");
        return ResponseEntity.ok(admin);
    }

    @GetMapping("/getAllTrainers")
    public ResponseEntity<List<UserDto>> getAllTrainers(){
        List<UserDto> trainers = userService.findByRole("TRAINER");
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/getAllSpa")
    public ResponseEntity<List<UserDto>> getAllSpa(){
        List<UserDto> spa = userService.findByRole("SPA");
        return ResponseEntity.ok(spa);
    }
}
