package Rest.Gym.RestGym.dto;

public class EmployeeRegistrationDto {


    private int id;
    private String username;
    private String fullname;
    private String email;
    private String specialization;
    private String bio;
    private String role;
    private String password;
    private String confirmPassword;

    public EmployeeRegistrationDto(String role, String bio, String specialization, String email, String fullname, String username) {
        this.role = role;
        this.bio = bio;
        this.specialization = specialization;
        this.email = email;
        this.fullname = fullname;
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

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
