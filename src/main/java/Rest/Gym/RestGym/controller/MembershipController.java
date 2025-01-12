package Rest.Gym.RestGym.controller;

import Rest.Gym.RestGym.dto.BookingRequest;
import Rest.Gym.RestGym.dto.MembershipDto;
import Rest.Gym.RestGym.dto.MembershipTypeRequest;
import Rest.Gym.RestGym.service.ConstructorService;
import Rest.Gym.RestGym.service.MembershipService;
import Rest.Gym.RestGym.dto.PersonMembershipDto;
import Rest.Gym.RestGym.service.PersonMembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/membership")
public class MembershipController {

    private final MembershipService membershipService;
    private final ConstructorService constructorService;
    private final PersonMembershipService personMembershipService;


    public MembershipController(MembershipService membershipService, ConstructorService constructorService, PersonMembershipService personMembershipService) {
        this.membershipService = membershipService;
        this.constructorService = constructorService;
        this.personMembershipService = personMembershipService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(membershipService.findAll());
    }

    @PostMapping("/getByType")
    public ResponseEntity<?> getByType(@RequestBody MembershipTypeRequest request) {
        MembershipDto membership = membershipService.getByType(request.getType());
        if (membership == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MembershipTypeRequest("Неверный тип абонемента"));
        }
        return ResponseEntity.ok(membership);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createMembership(@RequestBody MembershipDto membershipDto) {
        try {
            MembershipDto createdMembership = membershipService.createMembership(membershipDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(createdMembership);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при создании абонемента: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMembership(
            @PathVariable int id,
            @RequestBody MembershipDto membershipDto) {
        try {
            membershipDto.setId(id);
            MembershipDto updatedMembership = membershipService.updateMembership(membershipDto);
            if (updatedMembership == null) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Абонемент с ID " + id + " не найден");
            }
            return ResponseEntity.ok(updatedMembership);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при обновлении абонемента: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMembership(@PathVariable int id) {
        try {
            boolean deleted = membershipService.deleteMembership(id);
            if (!deleted) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Абонемент с ID " + id + " не найден");
            }
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при удалении абонемента: " + e.getMessage());
        }
    }

    @PostMapping("/constructor")
    public ResponseEntity<?> createConstructorMembership(
            @RequestParam Integer personId,
            @RequestParam int weeks,
            @RequestParam int gymVisitsPerWeek,
            @RequestParam int spaVisitsPerWeek) {
        try {
            PersonMembershipDto assignedMembership = constructorService.createConstructorMembership(
                    personId, weeks, gymVisitsPerWeek, spaVisitsPerWeek
            );
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(assignedMembership);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Ошибка при создании конструктора абонемента: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Внутренняя ошибка сервера: " + e.getMessage());
        }
    }

    @PostMapping("/assign/{personId}/{membershipId}")
    public ResponseEntity<?> assignMembership(
            @PathVariable Integer personId,
            @PathVariable Integer membershipId) {
        try {
            PersonMembershipDto dto = personMembershipService.assignMembership(personId, membershipId);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/validate-booking")
    public ResponseEntity<?> validateBooking(@RequestBody BookingRequest request) {
        try {
            boolean isValid = personMembershipService.validateAndUseVisit(
                    request.getPersonId(),
                    request.getType()
            );
            return ResponseEntity.ok().body("Booking validated and visit recorded");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/status/{userId}")
    public ResponseEntity<String> getUserStatus(@PathVariable Integer userId) {
        String status = personMembershipService.getMembershipStatus(userId);
        return ResponseEntity.ok(status);
    }
}
