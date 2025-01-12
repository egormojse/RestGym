package Rest.Gym.RestGym.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "person_membership")
public class PersonMembership {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "person_id")
    private Integer personId;

    @Column(name = "membership_id")
    private Integer membershipId;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "gym_visits")
    private Integer gymVisits = 0;

    @Column(name = "spa_visits")
    private Integer spaVisits = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public Integer getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(Integer membershipId) {
        this.membershipId = membershipId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getGymVisits() {
        return gymVisits;
    }

    public void setGymVisits(Integer gymVisits) {
        this.gymVisits = gymVisits;
    }

    public Integer getSpaVisits() {
        return spaVisits;
    }

    public void setSpaVisits(Integer spaVisits) {
        this.spaVisits = spaVisits;
    }
}
