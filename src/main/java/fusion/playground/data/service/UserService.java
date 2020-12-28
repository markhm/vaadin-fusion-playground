package fusion.playground.data.service;

import fusion.playground.data.entity.User;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.service.OktaService;
import fusion.playground.views.user.UserVO;
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

    @Autowired(required = false) private OktaService oktaService;

    // private List<User> cachedUsers;

    @Autowired
    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.oktaService = oktaService;

        // cachedUsers = new LinkedList<>();
    }

    @Override
    public UserRepository getRepository()
    {
        return this.userRepository;
    }

    public void createUser(UserVO userVO)
    {
        try
        {
            com.okta.sdk.resource.user.User userInOkta = oktaService.createUserInOkta(userVO);

            log.info("Created user in Okta: "+userInOkta);
        }
        catch(Exception exception)
        {
            log.error("Could not create user in Okta." + exception);
        }
    }

    public User findByOktaUserId(String oktaUserId)
    {
        log.info("Finding user based on OktaUserId: "+oktaUserId);
        User user = null;

        Optional<User> maybeUser = getRepository().findByOktaUserId(oktaUserId);

        if (!maybeUser.isPresent())
        {
            log.info("user not yet found in mongoDB repo, so retrieving details from Okta.");
            com.okta.sdk.resource.user.User oktaUser = oktaService.findByOktaUserId(oktaUserId);
            user = User.createFrom(oktaUser);

            getRepository().save(user);
        }
        else
        {
            user = maybeUser.get();
            log.info("User (" + user.username() + ") was found, returning.");
        }

        return user;
    }

//    public User findByUsername(String username)
//    {
//        User result = getRepository().findByUsername(username).get();
//        return result;
//    }

}
