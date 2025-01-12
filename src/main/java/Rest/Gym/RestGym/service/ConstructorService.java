package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.MembershipDto;
import Rest.Gym.RestGym.dto.PersonMembershipDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class ConstructorService {
    private static final int MAX_WEEKS = 48;
    private static final int MAX_VISITS_PER_WEEK = 7;
    private static final String CONSTRUCTOR_TYPE = "Конструктор";

    private final MembershipService membershipService;
    private final PersonMembershipService personMembershipService;

    public ConstructorService(MembershipService membershipService,
                              PersonMembershipService personMembershipService) {
        this.membershipService = membershipService;
        this.personMembershipService = personMembershipService;
    }

    @Transactional
    public PersonMembershipDto createConstructorMembership(Integer personId,
                                                           int weeks,
                                                           int gymVisitsPerWeek,
                                                           int spaVisitsPerWeek) {
        validateConstructorParameters(weeks, gymVisitsPerWeek, spaVisitsPerWeek);

        // Получаем базовый шаблон конструктора из базы
        MembershipDto templateMembership = membershipService.getByType(CONSTRUCTOR_TYPE);
        if (templateMembership == null) {
            throw new IllegalStateException("Шаблон конструктора не найден в базе данных");
        }

        // Рассчитываем количество посещений и даты
        int totalGymVisits = weeks * gymVisitsPerWeek;
        int totalSpaVisits = weeks * spaVisitsPerWeek;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusWeeks(weeks);

        // Создаем DTO для personMembership с нашими значениями
        PersonMembershipDto constructorMembership = new PersonMembershipDto();
        constructorMembership.setPersonId(personId);
        constructorMembership.setMembershipId(templateMembership.getId());
        constructorMembership.setStartDate(startDate);
        constructorMembership.setEndDate(endDate);
        constructorMembership.setGymVisits(totalGymVisits);
        constructorMembership.setSpaVisits(totalSpaVisits);

        // Передаем в сервис для сохранения
        return personMembershipService.assignCustomMembership(constructorMembership);
    }

    private void validateConstructorParameters(int weeks, int gymVisitsPerWeek, int spaVisitsPerWeek) {
        if (weeks < 1 || weeks > MAX_WEEKS) {
            throw new IllegalArgumentException("Количество недель должно быть от 1 до " + MAX_WEEKS);
        }
        if (gymVisitsPerWeek < 0 || gymVisitsPerWeek > MAX_VISITS_PER_WEEK) {
            throw new IllegalArgumentException("Количество посещений зала в неделю не может превышать " + MAX_VISITS_PER_WEEK);
        }
        if (spaVisitsPerWeek < 0 || spaVisitsPerWeek > MAX_VISITS_PER_WEEK) {
            throw new IllegalArgumentException("Количество посещений спа в неделю не может превышать " + MAX_VISITS_PER_WEEK);
        }
        if (gymVisitsPerWeek == 0 && spaVisitsPerWeek == 0) {
            throw new IllegalArgumentException("Необходимо указать хотя бы один тип посещения (зал или спа)");
        }
    }
}