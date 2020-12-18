package fusion.playground.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SurveyResultTest
{
    @Test
    public void testEquals()
    {
        SurveyResult surveyResult1 = new SurveyResult();
        surveyResult1.status(SurveyResult.SurveyResultStatus.created);

        Assertions.assertEquals(true, surveyResult1.equals(surveyResult1));
        Assertions.assertEquals(true, surveyResult1.hashCode() == (surveyResult1.hashCode()));

        SurveyResult surveyResult2 = new SurveyResult();
        surveyResult2.status(SurveyResult.SurveyResultStatus.complete);

        Assertions.assertEquals(false, surveyResult1.equals(surveyResult2));
        Assertions.assertEquals(false, surveyResult1.hashCode() == (surveyResult2.hashCode()));
    }

}
