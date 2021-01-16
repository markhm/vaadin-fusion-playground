package fusion.playground.security;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import fusion.playground.security.flow.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener
{
    private static Log log = LogFactory.getLog(ConfigureUIServiceInitListener.class);

    @Override
    public void serviceInit(ServiceInitEvent event)
    {
        event.getSource().addUIInitListener(uiEvent ->
        {
            final UI ui = uiEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });
    }

    /**
     * Reroutes the user if (s)he is not authorized to access the view.
     *
     * @param event before navigation event with event details
     */
    private void beforeEnter(BeforeEnterEvent event)
    {
        // This should have been taken care of in the SecurityConfiguration
//        if (event.getNavigationTarget().equals(FrontpageView.class))
//        {
//            return;
//        }

        // log.info("At beforeEnter, evaluating whether access in in place");

        if (!SecurityUtils.isAccessGranted(event.getNavigationTarget()))
        {
            if (SecurityUtils.isUserLoggedIn())
            {
                event.rerouteToError(NotFoundException.class);
            } else
            {
                event.rerouteTo("/login");
            }
        }
    }
}
