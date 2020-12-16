package fusion.playground.data.service;

import fusion.playground.data.entity.User;
import fusion.playground.service.OktaService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.Optional;

@Service
public class UserService extends MongoCrudService<User, String>
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private UserRepository userRepository;

    private OktaService oktaService;

    public UserService(@Autowired UserRepository userRepository,
                       @Autowired OktaService oktaService)
    {
        this.userRepository = userRepository;
        this.oktaService = oktaService;
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

    public Optional<User> findByOktaUserId(String id)
    {
        return getRepository().findByOktaUserId(id);
    }

    public Optional<User> findByUsername(String username)
    {
        return getRepository().findByUsername(username);
    }

    public User save(User user)
    {
        return getRepository().save(user);
    }

    public User createUser(User user)
    {
        Optional<User> optionalUser = userRepository.findByUsername(user.username());

        if (optionalUser.isPresent())
        {
            log.warn("A user with username '"+user.username()+"' already exists. Ignoring.");
            return null;
        }

        Optional<User> optionalUser2 = userRepository.findByEmailAddress(user.emailAddress());

        if (optionalUser2.isPresent())
        {
            log.warn("A user with emailAddress '"+user.emailAddress()+"' already exists. Ignoring.");
            return null;
        }

        com.okta.sdk.resource.user.User userInOkta = oktaService.createUserInOkta(user);

        log.info("Created user in Okta: "+userInOkta);

        return null;
    }


}
