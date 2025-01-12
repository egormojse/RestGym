package Rest.Gym.RestGym.repository;

import Rest.Gym.RestGym.model.PersonMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonMembershipRepository extends JpaRepository<PersonMembership, Integer> {
    Optional<PersonMembership> findByPersonId(Integer personId);
}
