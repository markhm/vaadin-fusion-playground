package fusion.playground.data.repository;

import fusion.playground.data.entity.Response;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends MongoRepository<Response, String> {

    // public Integer save(Answer answer);

}
