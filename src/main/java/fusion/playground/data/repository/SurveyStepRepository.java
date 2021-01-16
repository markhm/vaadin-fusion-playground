package fusion.playground.data.repository;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyStep;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyStepRepository extends MongoRepository<SurveyStep, String>
{
    // public List<Question> findAllByCategory(String category);

    // public int countAllByCategory(String category);

    // public Optional<Question> getByCategoryAndOrderNumber(String category, Integer orderNumber);

    // public Optional<Question> getBySurveyAndOrderNumber(Survey surveyName, Integer orderNumber);

}
