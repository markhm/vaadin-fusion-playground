package fusion.playground.data.service;

import fusion.playground.data.entity.Person;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface PersonRepository extends MongoRepository<Person, String>
{

}
