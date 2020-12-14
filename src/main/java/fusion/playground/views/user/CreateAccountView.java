package fusion.playground.views.user;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@Route(value = "create-account")
@PageTitle("Create account")
@CssImport("./views/css/generic-view.css")
public class CreateAccountView extends Div
{
    public CreateAccountView()
    {
        setId("create-account-view");
        add(new Label("Request a user account."));
    }

}
