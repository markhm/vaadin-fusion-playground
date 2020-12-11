package fusion.playground.data.service;

import fusion.playground.domain.User;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.Optional;

@Service
public class UserService extends MongoCrudService<User, String>
{
    private UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserRepository getRepository()
    {
        return this.userRepository;
    }

    public Optional<User> findById(String id)
    {
        return getRepository().findById(id);
    }

    public Optional<User> findByUsername(String username)
    {
        return getRepository().findByUsername(username);
    }

    public User save(User user)
    {
        return getRepository().save(user);
    }


}
