package fusion.playground.views.login;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.VaadinServletService;

import fusion.playground.security.flow.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;

@Route("server-login")
@PageTitle("Login")
public class LoginView extends VerticalLayout implements BeforeEnterObserver, ComponentEventListener<AbstractLogin.ForgotPasswordEvent>
{
    private static Log log = LogFactory.getLog(LoginView.class);

    // private LocaleChangeAgent localeChangeAgent = new LocaleChangeAgent();

    public static final String ROUTE = "server-login";

    private EmailDialog forgotPasswordDialog = null;

    private LoginForm loginForm = null;

    private FlexLayout footerWrapper = null;

    private String logoLocation = null;

    public LoginView()
    {
        addClassName("login-view");

        setSizeFull();
        setAlignItems(Alignment.CENTER); //
        setJustifyContentMode(JustifyContentMode.CENTER);

        // CrispAddition.addCrispChat();

        // loginForm = new LoginForm(createLoginI18n());
        loginForm = new LoginForm();
        loginForm.setAction("login"); //
        loginForm.addForgotPasswordListener(this);

        // logoLocation = VaadinServletService.getCurrent().resolveResource(MainLayout.LOGO_CURRENT_GREY, VaadinSession.getCurrent().getBrowser());
        logoLocation = VaadinServletService.getCurrent().resolveResource("/img/logo_large_EA_grey.png");
//        Image image = new Image(logoLocation, "Munis Server Login");
//        image.setHeight("200px");
//        add(image);

        // add(new H1("Software Strategizer"));
        add(loginForm);

//        footerWrapper = new InstanceViewUtil(localeChangeAgent).createFooterWrapper(InstanceViewUtil.FooterElementToHide.LogIn);
//        add(footerWrapper);
//        expand(footerWrapper);
    }

    @Override
    public void onComponentEvent(AbstractLogin.ForgotPasswordEvent forgotPasswordEvent)
    {
        forgotPasswordDialog = new EmailDialog(getTranslation("login.resetPassword"));
        forgotPasswordDialog.addDialogCloseActionListener(e -> {

            String email = forgotPasswordDialog.getEmail();
            log.info("Mail will be sent to: "+ email);
            forgotPasswordDialog.close();

        });
        forgotPasswordDialog.open();
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        // if the user is already logged in, we reroute to the main functionality page
        if (SecurityUtils.isUserLoggedIn())
        {
            log.error("NOT YET BEEN IMPLEMENTED");
            // event.forwardTo();
        }

        // inform the user about an authentication error
        if(!event.getLocation() //
                .getQueryParameters()
                .getParameters()
                .getOrDefault("error", Collections.emptyList())
                .isEmpty())
        {
            loginForm.setError(true);
        }

        if(!event.getLocation() //
                .getQueryParameters()
                .getParameters()
                .getOrDefault("disabled", Collections.emptyList())
                .isEmpty())
        {
            loginForm.setError(true);
        }
    }

    // https://vaadin.com/forum/thread/17942365/is-there-any-way-to-change-username-label-in-loginform
    private LoginI18n createLoginI18n()
    {
        LoginI18n i18n = LoginI18n.createDefault();

	/*  not sure if needed
	i18n.setHeader(new LoginI18n.Header());
	i18n.setForm(new LoginI18n.Form());
	i18n.setErrorMessage(new LoginI18n.ErrorMessage());
	*/

        // define all visible Strings to the values you want
        // this code is copied from above-linked example codes for Login
        // in a truly international application you would use i.e. `getTranslation(USERNAME)` instead of hardcoded string values. Make use of your I18nProvider

        // i18n.getHeader().setDescription("Descrição do aplicativo");


        // i18n.getHeader().setTitle("Nome do aplicativo");
        i18n.getForm().setTitle(getTranslation("login.title"));
        i18n.getForm().setUsername(getTranslation("login.username")); // this is the one you asked for.
        i18n.getForm().setPassword(getTranslation("login.password"));
        i18n.getForm().setSubmit(getTranslation("login.submit"));
        i18n.getForm().setForgotPassword(getTranslation("login.forgotPassword"));

        i18n.getErrorMessage().setTitle(getTranslation("login.error.title"));
        i18n.getErrorMessage().setMessage(getTranslation("login.error.message"));

        //        i18n.setAdditionalInformation(
//                "Caso necessite apresentar alguma informação extra para o usuário"
//                        + " (como credenciais padrão), este é o lugar.");

        return i18n;
    }

}
