package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.MembershipDto;
import Rest.Gym.RestGym.dto.PersonMembershipDto;
import Rest.Gym.RestGym.enums.BookingType;
import Rest.Gym.RestGym.model.PersonMembership;
import Rest.Gym.RestGym.repository.PersonMembershipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class PersonMembershipService
{
    private final PersonMembershipRepository personMembershipRepository;
    private final MembershipService membershipService;

    public PersonMembershipService(PersonMembershipRepository personMembershipRepository,
                                   MembershipService membershipService) {
        this.personMembershipRepository = personMembershipRepository;
        this.membershipService = membershipService;
    }

    @Transactional
    public PersonMembershipDto assignMembership(Integer personId, Integer membershipId) {
        MembershipDto membershipDto = membershipService.findById(membershipId);
        if (membershipDto == null) {
            throw new IllegalArgumentException("Membership not found");
        }

        Optional<PersonMembership> existingMembership = personMembershipRepository.findByPersonId(personId);
        PersonMembership personMembership;

        if (existingMembership.isPresent()) {
            personMembership = existingMembership.get();
            LocalDate currentEndDate = personMembership.getEndDate();

            if (currentEndDate.isBefore(LocalDate.now())) {
                personMembership.setStartDate(LocalDate.now());
                personMembership.setEndDate(LocalDate.now().plusMonths(1));
            } else {
                personMembership.setEndDate(currentEndDate.plusMonths(1));
            }

            personMembership.setGymVisits(personMembership.getGymVisits() + membershipDto.getGymVisits());
            personMembership.setSpaVisits(personMembership.getSpaVisits() + membershipDto.getSpaVisits());
        } else {
            personMembership = new PersonMembership();
            personMembership.setPersonId(personId);
            personMembership.setMembershipId(membershipId);
            personMembership.setStartDate(LocalDate.now());
            personMembership.setEndDate(LocalDate.now().plusMonths(1));
            personMembership.setGymVisits(membershipDto.getGymVisits());
            personMembership.setSpaVisits(membershipDto.getSpaVisits());
        }

        PersonMembership savedMembership = personMembershipRepository.save(personMembership);
        return convertToDto(savedMembership);
    }

    @Transactional
    public boolean validateAndUseVisit(Integer personId, BookingType type) {
        PersonMembership membership = personMembershipRepository.findByPersonId(personId)
                .orElseThrow(() -> new IllegalStateException("No active membership found"));

        // Проверяем срок действия абонемента
        LocalDate currentDate = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, membership.getEndDate());

        if (daysRemaining < 0) {
            throw new IllegalStateException("Membership expired " + Math.abs(daysRemaining) + " days ago");
        }

        // Проверяем и списываем визиты
        switch (type) {
            case GYM:
                if (membership.getGymVisits() <= 0) {
                    throw new IllegalStateException("No gym visits remaining. Days left: " + daysRemaining);
                }
                membership.setGymVisits(membership.getGymVisits() - 1);
                break;
            case SPA:
                if (membership.getSpaVisits() <= 0) {
                    throw new IllegalStateException("No spa visits remaining. Days left: " + daysRemaining);
                }
                membership.setSpaVisits(membership.getSpaVisits() - 1);
                break;
        }

        personMembershipRepository.save(membership);
        return true;
    }

    // Метод для получения информации о сроке действия абонемента
    public String getMembershipStatus(Integer personId) {
        PersonMembership membership = personMembershipRepository.findByPersonId(personId)
                .orElseThrow(() -> new IllegalStateException("No membership found"));

        LocalDate currentDate = LocalDate.now();
        long daysRemaining = ChronoUnit.DAYS.between(currentDate, membership.getEndDate());
        long totalDays = ChronoUnit.DAYS.between(membership.getStartDate(), membership.getEndDate());
        long daysUsed = ChronoUnit.DAYS.between(membership.getStartDate(), currentDate);

        return String.format("Membership status:\n" +
                        "Total duration: %d days\n" +
                        "Days used: %d\n" +
                        "Days remaining: %d\n" +
                        "Gym visits remaining: %d\n" +
                        "Spa visits remaining: %d",
                totalDays, daysUsed, daysRemaining,
                membership.getGymVisits(),
                membership.getSpaVisits());
    }

    @Transactional
    public PersonMembershipDto assignCustomMembership(PersonMembershipDto customMembership) {
        Optional<PersonMembership> existingMembership = personMembershipRepository.findByPersonId(customMembership.getPersonId());
        PersonMembership personMembership;

        if (existingMembership.isPresent()) {
            personMembership = existingMembership.get();
            LocalDate currentEndDate = personMembership.getEndDate();

            if (currentEndDate.isBefore(LocalDate.now())) {
                // Если текущий абонемент истек, создаем новый период
                personMembership.setStartDate(customMembership.getStartDate());
                personMembership.setEndDate(customMembership.getEndDate());
            } else {
                // Если текущий абонемент активен, добавляем посещения и продлеваем срок
                personMembership.setEndDate(currentEndDate.plus(
                        ChronoUnit.WEEKS.between(
                                customMembership.getStartDate(),
                                customMembership.getEndDate()
                        ),
                        ChronoUnit.WEEKS
                ));
            }
            personMembership.setGymVisits(personMembership.getGymVisits() + customMembership.getGymVisits());
            personMembership.setSpaVisits(personMembership.getSpaVisits() + customMembership.getSpaVisits());
        } else {
            personMembership = new PersonMembership();
            personMembership.setPersonId(customMembership.getPersonId());
            personMembership.setMembershipId(customMembership.getMembershipId());
            personMembership.setStartDate(customMembership.getStartDate());
            personMembership.setEndDate(customMembership.getEndDate());
            personMembership.setGymVisits(customMembership.getGymVisits());
            personMembership.setSpaVisits(customMembership.getSpaVisits());
        }

        PersonMembership savedMembership = personMembershipRepository.save(personMembership);
        return convertToDto(savedMembership);
    }

    private PersonMembershipDto convertToDto(PersonMembership entity) {
        PersonMembershipDto dto = new PersonMembershipDto();
        dto.setId(entity.getId());
        dto.setPersonId(entity.getPersonId());
        dto.setMembershipId(entity.getMembershipId());
        dto.setStartDate(entity.getStartDate());
        dto.setEndDate(entity.getEndDate());
        dto.setGymVisits(entity.getGymVisits());
        dto.setSpaVisits(entity.getSpaVisits());
        return dto;
    }
}
