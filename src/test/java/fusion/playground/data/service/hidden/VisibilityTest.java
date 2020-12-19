package fusion.playground.data.service.hidden;

import fusion.playground.data.entity.SurveyInfo;
import fusion.playground.data.service.AbstractServiceLayerTest;
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
        List<SurveyInfo> surveysAll = surveyService.getAvailableSurveysForOktaUserId("00u1f5copoLl1g5Y35d6");
        Assertions.assertEquals(4, surveysAll.size());

        // testuser
        List<SurveyInfo> surveysLimited = surveyService.getAvailableSurveysForOktaUserId("00u28osriV7V5f7pM5d6");
        Assertions.assertEquals(3, surveysLimited.size());

        // MasterVFP
        List<SurveyInfo> surveysQuestion = surveyService.getAvailableSurveysForOktaUserId("00u2rdeq9d17E6Z9N5d6");
        Assertions.assertEquals(3, surveysQuestion.size());


    }

}
