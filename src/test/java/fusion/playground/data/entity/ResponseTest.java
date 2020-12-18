package fusion.playground.data.entity;

import fusion.playground.data.service.AbstractServiceLayerTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ResponseTest extends AbstractServiceLayerTest
{
    @Test
    public void testEqualsHashcode()
    {
        Response response1 = new Response();
        response1.response("something");

        responseService.update(response1);

        Assertions.assertEquals(true, response1.equals(response1));
        Assertions.assertEquals(true, response1.hashCode() == (response1.hashCode()));

        Response response2 = new Response();
        response2.response("something else");

        responseService.update(response2);

        Assertions.assertEquals(false, response1.equals(response2));
        Assertions.assertEquals(false, response1.hashCode() == (response2.hashCode()));
    }

}
