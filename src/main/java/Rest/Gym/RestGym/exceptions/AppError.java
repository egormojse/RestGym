package Rest.Gym.RestGym.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class AppError {
    private String message;
    private int code;
    private Date timestamp;

    public AppError(int code, String message) {
        this.message = message;
        this.code = code;
        this.timestamp = new Date();
    }
}
