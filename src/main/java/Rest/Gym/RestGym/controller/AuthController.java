package Rest.Gym.RestGym.controller;

import Rest.Gym.RestGym.dto.JwtRequest;
import Rest.Gym.RestGym.dto.RegistrationUserDto;
import Rest.Gym.RestGym.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            value = "/getToken",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )

    public ResponseEntity<?> getToken(@RequestBody JwtRequest jwtRequest) {
        return authService.getToken(jwtRequest);
    }
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        return authService.register(registrationUserDto);
    }
}