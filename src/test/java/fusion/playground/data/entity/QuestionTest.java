package fusion.playground.data.entity;

import fusion.playground.data.service.AbstractServiceLayerTest;
import fusion.playground.data.service.SurveySessionService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QuestionTest extends AbstractServiceLayerTest
{
    private static Log log = LogFactory.getLog(QuestionTest.class);

    @Test
    public void testEqualsHashcode()
    {
        Question question1 = new Question();
        question1.text("example");

        questionService.update(question1);

        log.info("question1: "+question1);

        Assertions.assertEquals(true, question1.equals(question1));
        Assertions.assertEquals(true, question1.hashCode() == (question1.hashCode()));

        // ----------------------------------------

        Question question2 = new Question();
        question2.text("example");

        questionService.update(question2);

        log.info("question2: "+question2);

        Assertions.assertEquals(false, question1.equals(question2));
        Assertions.assertEquals(false, question1.hashCode() == (question2.hashCode()));
    }
}
