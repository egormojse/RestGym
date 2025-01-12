package Rest.Gym.RestGym.dto;

public class MembershipDto {

    private int id;
    private String type;
    private int gymVisits;
    private int spaVisits;
    private double price;

    public MembershipDto(Integer id, String type, int gymVisits, int spaVisits, double price) {
        this.id = id;
        this.type = type;
        this.gymVisits = gymVisits;
        this.spaVisits = spaVisits;
        this.price = price;
    }

    public MembershipDto() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getGymVisits() {
        return gymVisits;
    }

    public void setGymVisits(int gymVisits) {
        this.gymVisits = gymVisits;
    }

    public int getSpaVisits() {
        return spaVisits;
    }

    public void setSpaVisits(int spaVisits) {
        this.spaVisits = spaVisits;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
