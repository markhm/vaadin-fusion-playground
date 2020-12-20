package fusion.playground.views.survey;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

import javax.annotation.security.RolesAllowed;

@Route("add-survey-server")
@Secured(value= {"USER"})
@RolesAllowed(value = {"openid"})
public class AddSurveyView extends VerticalLayout
{
    public AddSurveyView()
    {
        setId("add-survey-view");

        add(new H3("Add survey"));
    }

}
