package fusion.playground.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AchievementTest
{
    @Test
    public void testEquals()
    {
        Achievement achievement1 = new Achievement();
        achievement1.description("Some description");

        Assertions.assertEquals(true, achievement1.equals(achievement1));
        Assertions.assertEquals(true, achievement1.hashCode() == (achievement1.hashCode()));

        Achievement achievement2 = new Achievement();
        achievement1.description("Another description");
        // person2.text("example");

        Assertions.assertEquals(false, achievement1.equals(achievement2));
        Assertions.assertEquals(false, achievement1.hashCode() == (achievement2.hashCode()));
    }
}
