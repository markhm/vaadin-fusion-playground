package fusion.playground.service;

import com.okta.sdk.authc.credentials.TokenClientCredentials;
import com.okta.sdk.client.Client;
import com.okta.sdk.client.Clients;
import com.okta.sdk.resource.user.User;
import com.okta.sdk.resource.user.UserBuilder;
import org.springframework.stereotype.Service;

@Service
public class OktaService
{
    // https://github.com/okta/okta-sdk-java#create-a-user

    String API_TOKEN = "00xUUBE9HCF5hgnCWc3cYYePqASqCN7muGzozanDIH";

    Client client = Clients.builder()
            .setOrgUrl("https://dev-8673725.okta.com/")  // e.g. https://dev-123456.okta.com
            .setClientCredentials(new TokenClientCredentials(API_TOKEN))
            .build();

    public OktaService()
    {
    }

    public User createUserInOkta(fusion.playground.data.entity.User user)
    {
        User userInOkta = UserBuilder.instance()
                .setEmail(user.emailAddress())
                .setLogin(user.emailAddress())
                .setFirstName(user.username())
                .setLastName("Not needed")
                .setActive(false) // becomes a staged user
                .buildAndCreate(client);

        String vaadinFusionPlaygroundGroup = "00g28nngsAJ6XnadX5d6";

        userInOkta.addToGroup(vaadinFusionPlaygroundGroup);
        userInOkta.update();

        return userInOkta;
    }

}
