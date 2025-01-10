package Rest.Gym.RestGym.controller;

import Rest.Gym.RestGym.dto.BookingDTO;
import Rest.Gym.RestGym.model.Booking;
import Rest.Gym.RestGym.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.ServiceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {


    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDTO) throws ServiceNotFoundException {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable int userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Booking>> getEmployeeBookings(@PathVariable int employeeId) {
        return ResponseEntity.ok(bookingService.getEmployeeBookings(employeeId));
    }

    @GetMapping("/employee/{employeeId}/schedule")
    public ResponseEntity<List<Booking>> getEmployeeSchedule(
            @PathVariable int employeeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return ResponseEntity.ok(bookingService.getEmployeeBookingsByDateRange(employeeId, startTime, endTime));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @PathVariable int id,
            @RequestBody BookingDTO bookingDTO) throws ServiceNotFoundException {
        return ResponseEntity.ok(bookingService.updateBooking(id, bookingDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable int id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok().build();
    }

}
