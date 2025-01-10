package Rest.Gym.RestGym.model;

import jakarta.persistence.*;

@Table(name = "membership_type")
@Entity
public class Membership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="type")
    private String type;

    @Column(name="gym_visits")
    private int gymVisits;

    @Column(name="spa_visits")
    private int spaVisits;

    @Column(name="price")
    private double price;

    public Membership() {}

    public Membership(int id, String type, double price, int gymVisits, int spaVisits) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.gymVisits = gymVisits;
        this.spaVisits = spaVisits;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSpaVisits() {
        return spaVisits;
    }

    public void setSpaVisits(int spaVisits) {
        this.spaVisits = spaVisits;
    }

    public int getGymVisits() {
        return gymVisits;
    }

    public void setGymVisits(int gymVisits) {
        this.gymVisits = gymVisits;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
