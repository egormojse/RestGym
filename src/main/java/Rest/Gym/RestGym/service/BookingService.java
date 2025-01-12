package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.BookingDTO;
import Rest.Gym.RestGym.enums.BookingType;
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
    private final PersonMembershipService personMembershipService;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          UserService userService,
                          ServiceEntityService serviceEntityService, PersonMembershipService personMembershipService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.serviceEntityService = serviceEntityService;
        this.personMembershipService = personMembershipService;
    }

    public Booking createBooking(BookingDTO bookingDTO) throws ServiceNotFoundException {
        // First validate the booking time
        validateBookingTime(bookingDTO.getEmployeeId(), bookingDTO.getDateTime());

        // Get service type to determine if it's GYM or SPA
        Rest.Gym.RestGym.model.Service service = serviceEntityService.getServiceById(bookingDTO.getServiceId());
        BookingType bookingType = determineBookingType(service); // You'll need to implement this

        // Validate membership and deduct visit
        try {
            boolean isValid = personMembershipService.validateAndUseVisit(
                    bookingDTO.getUserId(),
                    bookingType
            );

            if (!isValid) {
                throw new BookingException("Недостаточно посещений или абонемент истек");
            }

            // If validation passed, create the booking
            Booking booking = new Booking();
            populateBookingFromDTO(booking, bookingDTO);
            return bookingRepository.save(booking);

        } catch (IllegalStateException e) {
            // Convert membership service exceptions to booking exceptions
            throw new BookingException(e.getMessage());
        }
    }

    // Helper method to determine booking type based on service
    private BookingType determineBookingType(Rest.Gym.RestGym.model.Service service) {
        // Implement logic to determine if service is GYM or SPA
        // For example:
        if (service.getType().equals("GYM")) {
            return BookingType.GYM;
        } else if (service.getType().equals("SPA")) {
            return BookingType.SPA;
        }
        throw new BookingException("Неизвестный тип услуги");
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
