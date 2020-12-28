package fusion.playground.views.survey;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.annotation.security.RolesAllowed;

@Route("addSurvey-survey-server")
@Secured(value= {"SCOPE_openid"}) // USER
// @RolesAllowed(value = {"SCOPE_openid"})
// SCOPE_openid, SCOPE_email
@PreAuthorize("hasAuthority('SCOPE_profile')")
public class AddSurveyView extends VerticalLayout
{
    public AddSurveyView()
    {
        setId("addSurvey-survey-view");

        add(new H3("Add survey"));
    }

}
