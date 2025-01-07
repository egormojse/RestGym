package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.RegistrationUserDto;
import Rest.Gym.RestGym.dto.UserDto;
import Rest.Gym.RestGym.model.User;
import Rest.Gym.RestGym.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь '%s' не найден", username)
                ));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }

    public User createNewUser(RegistrationUserDto registrationUserDto) {
        User newUser = new User();
        newUser.setUsername(registrationUserDto.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationUserDto.getPassword()));
        newUser.setEmail(registrationUserDto.getEmail());
        newUser.setFullname(registrationUserDto.getFullname());
        newUser.setRole("ROLE_USER");
        return userRepository.save(newUser);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserDto> findByRole(String role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getFullname(),
                        user.getEmail()
                ))
                .collect(Collectors.toList());
    }
}