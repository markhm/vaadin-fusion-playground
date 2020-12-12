package fusion.playground.data.service;

import fusion.playground.data.entity.Response;
import fusion.playground.data.entity.Survey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String>
{
    Survey findByName(String name);
}
