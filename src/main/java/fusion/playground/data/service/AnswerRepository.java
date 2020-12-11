package fusion.playground.data.service;

import fusion.playground.domain.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AnswerRepository extends MongoRepository<Answer, String> {

    // public Integer save(Answer answer);

}
