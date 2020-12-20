package fusion.playground.data.repository;

import fusion.playground.data.entity.Response;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.User;
import fusion.playground.data.entity.Visibility;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends MongoRepository<Survey, String>
{
    @Deprecated
    Survey findByName(String name);

    @Deprecated
    Survey findByCategoryAndName(String category, String name);

    List<Survey> findAllByVisibility(Visibility visibility);

    List<Survey> findAllByOwnerAndVisibility(User owner, Visibility visibility);
}
