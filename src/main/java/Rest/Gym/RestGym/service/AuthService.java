package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.JwtRequest;
import Rest.Gym.RestGym.dto.JwtResponse;
import Rest.Gym.RestGym.dto.RegistrationUserDto;
import Rest.Gym.RestGym.exceptions.AppError;
import Rest.Gym.RestGym.model.User;
import Rest.Gym.RestGym.util.JwtTokenUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, JwtTokenUtils jwtTokenUtils, AuthenticationManager authenticationManager, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtTokenUtils = jwtTokenUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<?> getToken(JwtRequest jwtRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );

            UserDetails user = userService.loadUserByUsername(jwtRequest.getUsername());
            String token = jwtTokenUtils.generateToken(user);

            return ResponseEntity
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new JwtResponse(token));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new AppError(HttpStatus.UNAUTHORIZED.value(), "Неверный логин или пароль"));
        }
    }

    public ResponseEntity<?> register(@RequestBody RegistrationUserDto registrationUserDto) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("Пароли не совпадают");
        }

        // Добавляем логирование
        Optional<User> existingUser = userService.findByUsername(registrationUserDto.getUsername());
        System.out.println("Существующий пользователь: " + existingUser);

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Пользователь с таким именем уже существует");
        }

        try {
            User user = userService.createNewUser(registrationUserDto);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при регистрации: " + e.getMessage());
        }
    }
}

