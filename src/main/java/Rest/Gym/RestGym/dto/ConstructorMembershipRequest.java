package Rest.Gym.RestGym.dto;

public class ConstructorMembershipRequest {
    private int weeks;
    private int gymVisitsPerWeek;
    private int spaVisitsPerWeek;

    // Getters and Setters
    public int getWeeks() { return weeks; }
    public void setWeeks(int weeks) { this.weeks = weeks; }

    public int getGymVisitsPerWeek() { return gymVisitsPerWeek; }
    public void setGymVisitsPerWeek(int gymVisitsPerWeek) { this.gymVisitsPerWeek = gymVisitsPerWeek; }

    public int getSpaVisitsPerWeek() { return spaVisitsPerWeek; }
    public void setSpaVisitsPerWeek(int spaVisitsPerWeek) { this.spaVisitsPerWeek = spaVisitsPerWeek; }
}
