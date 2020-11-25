package fusion.playground.data.service;

import fusion.playground.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    // public Integer save(Answer answer);

}
