package Rest.Gym.RestGym.dto;

import Rest.Gym.RestGym.enums.BookingType;

import java.time.LocalDateTime;

public class BookingRequest {
    private Integer personId;
    private BookingType type;
    private LocalDateTime bookingTime;

    public BookingRequest(Integer personId, BookingType type, LocalDateTime bookingTime) {
        this.personId = personId;
        this.type = type;
        this.bookingTime = bookingTime;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public BookingType getType() {
        return type;
    }

    public void setType(BookingType type) {
        this.type = type;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }
}
