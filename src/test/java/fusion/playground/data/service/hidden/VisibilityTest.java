package fusion.playground.data.service.hidden;

import fusion.playground.data.entity.SurveyInfo;
import fusion.playground.data.service.AbstractServiceLayerTest;
import fusion.playground.service.SomeOktaUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VisibilityTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(VisibilityTest.class);

    public VisibilityTest()
    {}

    @Test
    public void testVisibility()
    {
        // markhm
        List<SurveyInfo> surveysAll = surveyService.getAvailableSurveysForOktaUserId(SomeOktaUser.HIDDEN_USER_OKTA_ID);
        Assertions.assertEquals(4, surveysAll.size());

        // testuser
        List<SurveyInfo> surveysLimited = surveyService.getAvailableSurveysForOktaUserId(SomeOktaUser.DEFAULT_USER_OKTA_ID);
        Assertions.assertEquals(3, surveysLimited.size());

//        // MasterVFP
//        List<SurveyInfo> surveysQuestion = surveyService.getAvailableSurveysForOktaUserId(TestUser.ADMIN_USER_OKTA_ID);
//        Assertions.assertEquals(3, surveysQuestion.size());
    }

}
