package fusion.playground.views.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;

public class EmailDialog extends Dialog
{
    private EmailField emailField = null;

    public EmailDialog(String titleString)
    {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(FlexComponent.Alignment.CENTER); //
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // StaticViewUtil.renderLogo(layout);

        Label whitespace_0 = new Label("");
        whitespace_0.setHeight("5px");
        layout.add(whitespace_0);

        H3 title = new H3(titleString);
        layout.add(title);

        Label whitespace_2 = new Label("");
        whitespace_2.setHeight("10px");
        layout.add(whitespace_2);
        layout.add(getTranslation("login.provideEmail"));

        emailField = new EmailField();
        Button submitButton = new Button(getTranslation("login.submit"), e -> close());

        layout.add(emailField);
        layout.add(submitButton);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        this.add(layout);
        this.setWidth("500px");
        this.setHeight("450");
    }

    public String getEmail()
    {
        return emailField.getValue();
    }
}
