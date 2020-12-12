package fusion.playground.data.service;

import fusion.playground.data.entity.Response;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResponseRepository extends MongoRepository<Response, String> {

    // public Integer save(Answer answer);

}
