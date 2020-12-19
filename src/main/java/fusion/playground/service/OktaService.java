package fusion.playground.service;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import com.okta.sdk.resource.user.UserList;
import com.okta.sdk.resource.user.UserProfile;
import fusion.playground.data.service.SurveyService;
import fusion.playground.views.user.UserVO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OktaService
{
    private static Log log = LogFactory.getLog(OktaService.class);

    private static final String API_TOKEN_KEY = "okta_API_TOKEN";
    private static final String ORG_URL_KEY = "okta_OrgUrl";
    private static final String VFP_GROUP_KEY = "okta_VFP-group";

    private Environment env;
    private Client oktaClient;

    // https://github.com/okta/okta-sdk-java#create-a-user


    @Autowired
    public OktaService(Environment env)
    {
        this.env = env;

        oktaClient = Clients.builder()
                .setOrgUrl(env.getProperty(ORG_URL_KEY))  // e.g. https://dev-123456.okta.com
                .setClientCredentials(new TokenClientCredentials(env.getProperty(API_TOKEN_KEY)))
                .build();
    }

    public User createUserInOkta(UserVO user)
    {
        log.info("Trying to create user in Okta at " + env.getProperty(ORG_URL_KEY) + ".");

        User userInOkta = UserBuilder.instance()
                .setLogin(user.emailAddress())
                .setPassword(user.password().toCharArray())
                .setEmail(user.emailAddress())
                .putProfileProperty(fusion.playground.data.entity.User.DISPLAY_NAME, user.username())
                .putProfileProperty(fusion.playground.data.entity.User.DATE_OF_BIRTH, user.dateOfBirth().toString())
                .setFirstName(user.username())
                .setLastName("Unused")
                .setActive(true)
                .buildAndCreate(oktaClient);

        userInOkta.addToGroup(env.getProperty(VFP_GROUP_KEY));
        userInOkta.update();

        return userInOkta;
    }

    public User findByOktaUserId(String oktaUserId)
    {
        User user = oktaClient.getUser(oktaUserId);

        return user;
    }

    public void setDateOfBirth(String oktaUserId, LocalDate dateOfBirth)
    {
        User user = oktaClient.getUser(oktaUserId);
        UserProfile profile = user.getProfile();
        profile.put(fusion.playground.data.entity.User.DATE_OF_BIRTH, dateOfBirth.toString());
        user.update();
    }


    public User findByEmailAddress(String emailAddress)
    {
        String filter = "profile.login eq \"" + emailAddress + "\"";

        User result = null;
        UserList users = oktaClient.listUsers(null, filter, null, null, null);
        result = users.single();

        return result;
    }


}
