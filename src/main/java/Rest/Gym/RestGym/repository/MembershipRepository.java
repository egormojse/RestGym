package Rest.Gym.RestGym.repository;

import Rest.Gym.RestGym.model.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Integer> {

    Membership findByType(String type);
    List<Membership> findAll();
}
