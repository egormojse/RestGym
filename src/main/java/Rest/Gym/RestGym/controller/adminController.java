package Rest.Gym.RestGym.controller;

import Rest.Gym.RestGym.dto.DashboardDto;
import Rest.Gym.RestGym.dto.EmployeeRegistrationDto;
import Rest.Gym.RestGym.service.AdminService;
import Rest.Gym.RestGym.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class adminController {

    private final AdminService adminService;
    private final UserService userService;

    public adminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDto> getDashboard() {
        return ResponseEntity.ok(adminService.getDashboardData());
    }

    @PostMapping("/employees")
    public ResponseEntity<EmployeeRegistrationDto> registerEmployee(@Validated @RequestBody EmployeeRegistrationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adminService.registerEmployee(dto));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id) {
        try {
            boolean deleted = userService.deleteUser(id);
            if (!deleted) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("человек с ID " + id + " не найден");
            }
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении: " + e.getMessage());
        }
    }
}
