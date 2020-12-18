package fusion.playground.data.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PersonTest
{
    @Test
    public void testEquals()
    {
        Person person1 = new Person();
        person1.email("person1");
        // person1.id("1");

        Assertions.assertEquals(true, person1.equals(person1));
        Assertions.assertEquals(true, person1.hashCode() == (person1.hashCode()));

        Person person2 = new Person();
        person2.email("person2");
        // person2.text("example");

        Assertions.assertEquals(false, person1.equals(person2));
        Assertions.assertEquals(false, person1.hashCode() == (person2.hashCode()));
    }
}
