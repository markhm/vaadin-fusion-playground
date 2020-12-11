package fusion.playground.data.service;

import fusion.playground.domain.PossibleAnswer;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface PossibleAnswerRepository extends MongoRepository<PossibleAnswer, String>
{
}
