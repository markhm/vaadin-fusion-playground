package fusion.playground.service;

import com.okta.sdk.resource.user.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Conditional;

import java.time.LocalDate;

// https://www.baeldung.com/junit-5-conditional-test-execution

// @SpringBootTest
public class OktaServiceTest
{
    private static Log log = LogFactory.getLog(OktaServiceTest.class);

    private OktaService oktaService;

    @Autowired
    public OktaServiceTest(OktaService oktaService)
    {
        this.oktaService = oktaService;
    }

    // @Test
    public void testFindUserByEmail()
    {
        String username = "testuser@test.com";
        User user = oktaService.findByEmailAddress(username);

        log.info("Found user by username " + username + ": "+user);
    }

    // @Test
    public void testFindUserByOktaUserId()
    {
        User user = oktaService.findByOktaUserId(SomeOktaUser.DEFAULT_USER_OKTA_ID);

        Object dateOfBirth = user.getProfile().get(fusion.playground.data.entity.User.DATE_OF_BIRTH);
        Object displayName = user.getProfile().get(fusion.playground.data.entity.User.DISPLAY_NAME);

        log.info("Found user by oktaUserId " + SomeOktaUser.DEFAULT_USER_OKTA_ID + ": "+user);
        log.info("");

        log.info("dateOfBirth: " + dateOfBirth);
        log.info("displayName: " + displayName);
    }

    // @Test
    public void testSetDateOfBirth()
    {

        LocalDate localDate = LocalDate.now().minusYears(25);
        oktaService.setDateOfBirth(SomeOktaUser.DEFAULT_USER_OKTA_ID, localDate);

    }

    // @Test
    public void localDateToString()
    {
        LocalDate localDate = LocalDate.now().minusYears(25);
        log.info("LocalDate: " + localDate);

    }
}
