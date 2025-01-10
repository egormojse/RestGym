package Rest.Gym.RestGym.repository;

import Rest.Gym.RestGym.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByUserId(int userId);
    List<Booking> findByEmployeeId(int employeeId);

    @Query("SELECT b FROM Booking b WHERE b.employee.id = :employeeId AND b.dateTime BETWEEN :startTime AND :endTime")
    List<Booking> findByEmployeeAndDateTimeBetween(int employeeId, LocalDateTime startTime, LocalDateTime endTime);

    boolean existsByEmployeeIdAndDateTime(int employeeId, LocalDateTime dateTime);
}