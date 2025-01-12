package Rest.Gym.RestGym.dto;

import java.time.LocalDate;

public class PersonMembershipDto {
    private Integer id;
    private Integer personId;
    private Integer membershipId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer gymVisits;
    private Integer spaVisits;
    public PersonMembershipDto() {}

    public PersonMembershipDto(Integer id, Integer personId, LocalDate startDate,
                               Integer membershipId, LocalDate endDate, Integer gymVisits,
                               Integer spaVisits) {
        this.id = id;
        this.personId = personId;
        this.startDate = startDate;
        this.membershipId = membershipId;
        this.endDate = endDate;
        this.gymVisits = gymVisits;
        this.spaVisits = spaVisits;
    }

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
