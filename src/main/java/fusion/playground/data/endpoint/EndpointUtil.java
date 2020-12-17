package fusion.playground.data.endpoint;

import com.vaadin.flow.server.VaadinRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EndpointUtil
{
    private static Log log = LogFactory.getLog(EndpointUtil.class);

    public static void logPrincipal(String description)
    {
        log.info("Request "+description+" is done by: "+VaadinRequest.getCurrent().getUserPrincipal().getName());
    }

}
