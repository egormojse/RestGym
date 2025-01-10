package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.DashboardDto;
import Rest.Gym.RestGym.dto.EmployeeRegistrationDto;
import Rest.Gym.RestGym.model.User;
import Rest.Gym.RestGym.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AdminService(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


    public EmployeeRegistrationDto registerEmployee(EmployeeRegistrationDto dto) {
        User newUser = new User();
        newUser.setUsername(dto.getUsername());
        newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        newUser.setEmail(dto.getEmail());
        newUser.setFullname(dto.getFullname());
        newUser.setBio(dto.getBio());
        newUser.setSpecialization(dto.getSpecialization());
        newUser.setRole(dto.getRole());
        userRepository.save(newUser);
        return dto;
    }

    public DashboardDto getDashboardData() {
        //TODO
        return new DashboardDto();
    }
}
