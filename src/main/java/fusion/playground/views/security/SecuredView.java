package fusion.playground.views.security;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Secured(value = {"USER"})
@Route("server-secured")
@PageTitle("Secured View")
public class SecuredView extends VerticalLayout {

    public SecuredView() {

        add(new H3("Secured view on the server (Flow)"));
    }

}
