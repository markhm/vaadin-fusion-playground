package fusion.playground.data.service;

import fusion.playground.domain.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String>
{
    public List<Question> findAllByCategory(String category);

    public int countAllByCategory(String category);

    public Optional<Question> getByCategoryAndOrderNumber(String category, Integer orderNumber);
}
