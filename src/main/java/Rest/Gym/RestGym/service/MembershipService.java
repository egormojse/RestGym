package Rest.Gym.RestGym.service;

import Rest.Gym.RestGym.dto.MembershipDto;
import Rest.Gym.RestGym.model.Membership;
import Rest.Gym.RestGym.repository.MembershipRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;

    public MembershipService(MembershipRepository membershipRepository) {
        this.membershipRepository = membershipRepository;
    }

    public List<MembershipDto> findAll() {
        List<Membership> memberships = membershipRepository.findAll();
        return memberships.stream()
                .map(membership -> new MembershipDto(
                        membership.getId(),
                        membership.getType(),
                        membership.getGymVisits(),
                        membership.getSpaVisits(),
                        membership.getPrice()
                ))
                .collect(Collectors.toList());
    }

    public MembershipDto getByType(String type) {
        Membership membership = membershipRepository.findByType(type);
        if (membership == null) {
            return null; // Возвращаем null если абонемент не найден
        }
        return new MembershipDto(
                membership.getId(),
                membership.getType(),
                membership.getGymVisits(),
                membership.getSpaVisits(),
                membership.getPrice());
    }

    public MembershipDto createMembership(MembershipDto membershipDto) {
        validateMembershipDto(membershipDto);
        Membership membership = convertToEntity(membershipDto);
        Membership savedMembership = membershipRepository.save(membership);
        return convertToDto(savedMembership);
    }

    public MembershipDto updateMembership(MembershipDto membershipDto) {
        if (!membershipRepository.existsById(membershipDto.getId())) {
            return null;
        }
        validateMembershipDto(membershipDto);
        Membership membership = convertToEntity(membershipDto);
        Membership updatedMembership = membershipRepository.save(membership);
        return convertToDto(updatedMembership);
    }

    public boolean deleteMembership(int id) {
        if (!membershipRepository.existsById(id)) {
            return false;
        }
        membershipRepository.deleteById(id);
        return true;
    }


    private void validateMembershipDto(MembershipDto membershipDto) {
        if (membershipDto.getType() == null || membershipDto.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Тип абонемента не может быть пустым");
        }
        if (membershipDto.getPrice() == 0 || membershipDto.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена должна быть положительным числом");
        }
        if (membershipDto.getSpaVisits() == 0 || membershipDto.getSpaVisits() <= 0) {
            throw new IllegalArgumentException("Посещения спа должны быть положительным числом");
        }
        if (membershipDto.getGymVisits() == 0 || membershipDto.getGymVisits() <= 0) {
            throw new IllegalArgumentException("Посещения зала должны быть положительным числом");
        }
    }

    private MembershipDto convertToDto(Membership membership) {
        return new MembershipDto(
                        membership.getId(),
                        membership.getType(),
                membership.getGymVisits(),
                membership.getSpaVisits(),
                membership.getPrice()
                );
    }

    private Membership convertToEntity(MembershipDto dto) {
        return new Membership(
                dto.getId(),
                dto.getType(),
                dto.getPrice(),
                dto.getGymVisits(),
                dto.getSpaVisits()
        );
    }

    public MembershipDto findById(Integer membershipId) {
        return convertToDto(membershipRepository.findById(membershipId).get());
    }

}
