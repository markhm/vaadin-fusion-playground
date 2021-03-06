package fusion.playground.data.repository;

import fusion.playground.data.entity.SurveyResult;
import fusion.playground.data.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyResultRepository extends MongoRepository<SurveyResult, String>
{
    public List<SurveyResult> findAllByUser(User user);

    public List<SurveyResult> findAllByUserAndComplete(User user, boolean complete);

    public List<SurveyResult> findAllByUserAndStatus(User user, SurveyResult.SurveyResultStatus status);
}
