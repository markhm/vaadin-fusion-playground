package fusion.playground.data.service.hidden;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyInfo;
import fusion.playground.data.entity.User;
import fusion.playground.data.service.AbstractServiceLayerTest;
import fusion.playground.service.SomeOktaUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class VisibilityTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(VisibilityTest.class);

    public VisibilityTest()
    {}

    @Test
    public void testVisibility()
    {
        printUsers();
        printSurveys();

        // markhm
        List<SurveyInfo> surveysAll = surveyService.getAvailableSurveysForOktaUserId(SomeOktaUser.HIDDEN_USER_OKTA_ID);
        Assertions.assertEquals(4, surveysAll.size());

        // testuser
        List<SurveyInfo> surveysLimited = surveyService.getAvailableSurveysForOktaUserId(SomeOktaUser.DEFAULT_USER_OKTA_ID);
        Assertions.assertEquals(3, surveysLimited.size());

        // MasterVFP
        List<Survey> surveysQuestion = surveyService.getOwnedSurveys(SomeOktaUser.HIDDEN_USER_OKTA_ID);
        Assertions.assertEquals(2, surveysQuestion.size());
    }

    private void printSurveys()
    {
        log.info("Number of surveys found: " + surveyService.count());

        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")));

        Page<Survey> surveys = surveyService.list(pageable);

        surveys.forEach(survey -> log.info("Found survey with (name, ownerId): (" + (survey.name() +", " + survey.ownerId()) + ")"));
        log.info("");
    }

    private void printUsers()
    {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(
                Sort.Order.asc("name"),
                Sort.Order.desc("id")));

        Page<User> users = userService.list(pageable);

        users.forEach(user -> log.info("Found user: " + user.toString()));
        log.info("");

    }



}
