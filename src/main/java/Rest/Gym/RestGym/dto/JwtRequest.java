package Rest.Gym.RestGym.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
