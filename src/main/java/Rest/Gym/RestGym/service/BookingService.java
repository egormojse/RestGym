package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.BookingDTO;
import Rest.Gym.RestGym.exceptions.BookingException;
import Rest.Gym.RestGym.model.Booking;
import Rest.Gym.RestGym.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ServiceEntityService serviceEntityService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          UserService userService,
                          ServiceEntityService serviceEntityService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.serviceEntityService = serviceEntityService;
    }

    public Booking createBooking(BookingDTO bookingDTO) throws ServiceNotFoundException {
        validateBookingTime(bookingDTO.getEmployeeId(), bookingDTO.getDateTime());

        Booking booking = new Booking();
        populateBookingFromDTO(booking, bookingDTO);
        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getEmployeeBookings(int employeeId) {
        return bookingRepository.findByEmployeeId(employeeId);
    }

    public List<Booking> getEmployeeBookingsByDateRange(int employeeId, LocalDateTime startTime, LocalDateTime endTime) {
        return bookingRepository.findByEmployeeAndDateTimeBetween(employeeId, startTime, endTime);
    }

    public Booking updateBooking(int id, BookingDTO bookingDTO) throws ServiceNotFoundException {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingException("Бронирование не найдено с ID: " + id));

        if (!booking.getDateTime().equals(bookingDTO.getDateTime())) {
            validateBookingTime(bookingDTO.getEmployeeId(), bookingDTO.getDateTime());
        }

        populateBookingFromDTO(booking, bookingDTO);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new BookingException("Бронирование не найдено с ID: " + id);
        }
        bookingRepository.deleteById(id);
    }

    private void validateBookingTime(int employeeId, LocalDateTime dateTime) {
        if (bookingRepository.existsByEmployeeIdAndDateTime(employeeId, dateTime)) {
            throw new BookingException("Выбранное время уже занято");
        }
    }

    private void populateBookingFromDTO(Booking booking, BookingDTO dto) throws ServiceNotFoundException {
        booking.setUser(userService.findById(dto.getUserId()));
        booking.setEmployee(userService.findById(dto.getEmployeeId()));
        booking.setService(serviceEntityService.getServiceById(dto.getServiceId()));
        booking.setDateTime(dto.getDateTime());
        booking.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
    }
}
