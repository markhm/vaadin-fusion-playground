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

    @Autowired
    public UserService( UserRepository userRepository, OktaService oktaService)
    {
        this.userRepository = userRepository;
        this.oktaService = oktaService;
    }

    @Override
    public UserRepository getRepository()
    {
        return this.userRepository;
    }

    public User findByOktaUserId(String id)
    {
        return getRepository().findByOktaUserId(id).get();
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
            log.warn("A userClaims with username '"+user.username()+"' already exists. Ignoring.");
            return null;
        }

        Optional<User> optionalUser2 = userRepository.findByEmailAddress(user.emailAddress());

        if (optionalUser2.isPresent())
        {
            log.warn("A userClaims with emailAddress '"+user.emailAddress()+"' already exists. Ignoring.");
            return null;
        }

        com.okta.sdk.resource.user.User userInOkta = oktaService.createUserInOkta(user);

        log.info("Created userClaims in Okta: "+userInOkta);

        return null;
    }


}
