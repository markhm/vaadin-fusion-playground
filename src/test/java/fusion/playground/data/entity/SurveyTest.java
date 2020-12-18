package fusion.playground.data.entity;

import fusion.playground.data.service.AbstractServiceLayerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SurveyTest extends AbstractServiceLayerTest
{
    @Test
    public void testEqualsHashcode()
    {
        Survey survey1 = new Survey();
        survey1.category("example");
        survey1.name("survey 1");

        surveyService.update(survey1);

        Assertions.assertEquals(true, survey1.equals(survey1));
        Assertions.assertEquals(true, survey1.hashCode() == (survey1.hashCode()));

        Survey survey2 = new Survey();
        survey2.category("example");
        survey2.name("survey 2");

        surveyService.update(survey2);

        Assertions.assertEquals(false, survey1.equals(survey2));
        Assertions.assertEquals(false, survey1.hashCode() == (survey2.hashCode()));
    }
}
