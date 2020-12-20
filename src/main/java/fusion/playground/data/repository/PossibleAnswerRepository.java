package fusion.playground.data.repository;

import fusion.playground.data.entity.PossibleAnswer;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface PossibleAnswerRepository extends MongoRepository<PossibleAnswer, String>
{
}
