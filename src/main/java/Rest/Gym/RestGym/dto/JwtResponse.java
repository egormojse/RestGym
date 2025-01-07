package Rest.Gym.RestGym.dto;

import lombok.Data;

@Data
public class JwtResponse {
    public JwtResponse(String token) {
        this.token = token;
    }

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
