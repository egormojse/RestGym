package Rest.Gym.RestGym.dto;

import lombok.Data;

@Data
public class RegistrationUserDto {

    private int id;
    private String username;
    private String fullname;
    private String email;
    private String password;
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
