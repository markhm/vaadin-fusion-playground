package fusion.playground.security.flow;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;

import fusion.playground.views.login.LoginView;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpSessionRequestCache that avoids saving internal framework requests.
 */
@Component
public class CustomRequestCache extends HttpSessionRequestCache
{
    /**
     * {@inheritDoc}
     * <p>
     * If the method is considered an internal request from the framework, we skip
     * saving it.
     *
     * @see SecurityUtils#isFrameworkInternalRequest(HttpServletRequest)
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response)
    {
        if (!SecurityUtils.isFrameworkInternalRequest(request))
        {
            super.saveRequest(request, response);
        }
    }

    public String resolveRedirectUrl()
    {
        SavedRequest savedRequest = getRequest(VaadinServletRequest.getCurrent().getHttpServletRequest(),
                VaadinServletResponse.getCurrent().getHttpServletResponse());
        if (savedRequest instanceof DefaultSavedRequest)
        {
            final String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI(); //
            // check for valid URI and prevent redirecting to the login view
            if (requestURI != null && !requestURI.isEmpty() && !requestURI.contains(LoginView.ROUTE))
            { //
                return requestURI.startsWith("/") ? requestURI.substring(1) : requestURI; //
            }
        }

        // if everything fails, redirect to the main view
        return "";
    }

}
