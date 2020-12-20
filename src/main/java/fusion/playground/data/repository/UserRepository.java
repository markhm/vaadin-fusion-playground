package fusion.playground.data.repository;

import fusion.playground.data.entity.User;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String>
{
    public Optional<User> findByUsername(String username);

    public Optional<User> findByOktaUserId(String oktaUserId);
}
