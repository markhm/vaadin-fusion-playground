package fusion.playground.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PossibleAnswerTest
{
    @Test
    public void testEquals()
    {
        PossibleAnswer possibleAnswer1 = new PossibleAnswer();
        possibleAnswer1.text("Possible answer 1");

        Assertions.assertEquals(true, possibleAnswer1.equals(possibleAnswer1));
        Assertions.assertEquals(true, possibleAnswer1.hashCode() == (possibleAnswer1.hashCode()));

        PossibleAnswer possibleAnswer2 = new PossibleAnswer();
        possibleAnswer2.text("Possible answer 2");

        Assertions.assertEquals(false, possibleAnswer1.equals(possibleAnswer2));
        Assertions.assertEquals(false, possibleAnswer1.hashCode() == (possibleAnswer2.hashCode()));
    }
}
