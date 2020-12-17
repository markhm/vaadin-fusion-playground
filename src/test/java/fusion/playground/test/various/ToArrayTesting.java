package fusion.playground.test.various;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ToArrayTesting
{
    @Test
    public void testToArray()
    {
        List<String> list = new ArrayList<>();

        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");

        String[] converted = list.stream().toArray(String[]::new);

        for (String s : converted)
        {
            System.out.println(s);
        }
    }

}
