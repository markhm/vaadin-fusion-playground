package fusion.playground.data.service;

import fusion.playground.domain.PossibleAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PossibleAnswerRepository extends JpaRepository<PossibleAnswer, Integer>
{
}
