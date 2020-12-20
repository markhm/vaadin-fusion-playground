package fusion.playground.service;

import com.okta.sdk.resource.user.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Conditional;

import java.time.LocalDate;

@SpringBootTest
@ConditionalOnProperty("run-integration-test")
public class OktaServiceTest
{
    private static Log log = LogFactory.getLog(OktaServiceTest.class);

    private OktaService oktaService;

    @Autowired
    public OktaServiceTest(OktaService oktaService)
    {
        this.oktaService = oktaService;
    }

    @Test
    public void testFindUserByEmail()
    {
        String username = "testuser@test.com";
        User user = oktaService.findByEmailAddress(username);

        log.info("Found user by username " + username + ": "+user);
    }

    @Test
    public void testFindUserByOktaUserId()
    {
        String oktaUserId = "00u28osriV7V5f7pM5d6";
        User user = oktaService.findByOktaUserId(oktaUserId);

        Object dateOfBirth = user.getProfile().get(fusion.playground.data.entity.User.DATE_OF_BIRTH);
        Object displayName = user.getProfile().get(fusion.playground.data.entity.User.DISPLAY_NAME);

        log.info("Found user by oktaUserId " + oktaUserId + ": "+user);
        log.info("");

        log.info("dateOfBirth: " + dateOfBirth);
        log.info("displayName: " + displayName);
    }

    @Test
    public void testSetDateOfBirth()
    {
        String oktaUserId = "00u28osriV7V5f7pM5d6";
        LocalDate localDate = LocalDate.now().minusYears(25);
        oktaService.setDateOfBirth(oktaUserId, localDate);

    }

    @Test
    public void localDateToString()
    {
        LocalDate localDate = LocalDate.now().minusYears(25);
        log.info("LocalDate: " + localDate);

    }
}
