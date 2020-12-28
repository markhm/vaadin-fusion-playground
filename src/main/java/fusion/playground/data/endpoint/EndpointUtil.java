package fusion.playground.data.endpoint;

import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.connect.VaadinConnectController;
import com.vaadin.flow.server.connect.VaadinConnectControllerConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

public class EndpointUtil
{
    private static Log log = LogFactory.getLog(EndpointUtil.class);

    public static void logAuthenticationContext(String description)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Auth = "+auth.toString());


        Optional<VaadinSession> maybeSession = Optional.ofNullable(VaadinSession.getCurrent());
        if (maybeSession.isPresent())
        {
            VaadinSession session = maybeSession.get();
            session.getService();

        }

    }

    private static void logPrincipal(String description)
    {
        Optional<VaadinRequest> vaadinRequest = Optional.ofNullable(VaadinRequest.getCurrent());

        if (vaadinRequest.isPresent())
        {
            Optional<Principal> maybePrincipal = Optional.ofNullable(VaadinRequest.getCurrent().getUserPrincipal());

            if (maybePrincipal.isPresent())
            {
                log.info("Request " + description + " is done by: " + maybePrincipal.get().getName());
            } else
            {
                log.info("AuthType: " + VaadinRequest.getCurrent().getAuthType());
            }
        }
        else
        {
            log.info("VaadinRequest is null");
        }
    }


}
