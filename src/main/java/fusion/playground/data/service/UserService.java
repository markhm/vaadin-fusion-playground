package fusion.playground.data.service;

import fusion.playground.domain.User;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

import java.util.Optional;

@Service
public class UserService extends CrudService<User, Integer>
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

    public Optional<User> findById(Integer id)
    {
        return getRepository().findById(id);
    }

    public User save(User user)
    {
        return getRepository().save(user);
    }


}
