package fusion.playground.data.service;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResponse;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyResponseRepository extends MongoRepository<SurveyResponse, String>
{
}
