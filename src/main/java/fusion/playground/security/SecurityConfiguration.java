package fusion.playground.security;

import com.okta.spring.boot.oauth.Okta;
import fusion.playground.security.flow.CustomRequestCache;
import fusion.playground.security.flow.SecurityUtils;
import fusion.playground.views.login.LoginView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Instructions for enabling HTTPS:
// https://www.thomasvitale.com/https-spring-boot-ssl-certificate/

// Setting up Spring Security
// https://vaadin.com/learn/tutorials/securing-your-app-with-spring-security/setting-up-spring-security

@EnableWebSecurity
@ConditionalOnWebApplication
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private static Log log = LogFactory.getLog(SecurityConfiguration.class);

    @Autowired
    private Environment environment;

    public SecurityConfiguration(@Autowired Environment environment)
    {
        this.environment = environment;
    }

    @Override
    public void configure(WebSecurity web) throws Exception
    {
        // @formatter:off
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/**/*.{js,html,css,webmanifest,png,js.map}",
                        "/icons/**", "/images/**", "/img/**", "/frontend/**", "/VAADIN/**");
        // @formatter:on
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // method used to be part of the Fusion security method, but extracting should not hurt
        requireSecureChannel(http);
        configureFusionSecurity(http);

        configureFlowSecurity(http);
    }

    private void requireSecureChannel(HttpSecurity http) throws Exception {

        Boolean developmentMode = environment.getProperty("developmentMode", Boolean.class);
        if (developmentMode) {
            // All requests are required to be secure.
            http.
                    requiresChannel().
                    anyRequest().
                    requiresSecure();
        } else // -> (!developmentMode)
        {
            // also on Heroku, things need to be secure
            http.
                    requiresChannel().
                    requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).
                    requiresSecure();
        }
    }

    protected void configureFusionSecurity(HttpSecurity http) throws Exception
    {
        // @formatter:off
        // Vaadin handles CSRF for its endpoints

        http.csrf().ignoringAntMatchers("/connect/**")
                .and()
                .authorizeRequests()
                .antMatchers("/create-account/**").permitAll()
                // allow access to everything, Vaadin will handle security
                .antMatchers("/**").permitAll()
                .and()
                .oauth2ResourceServer().jwt();
        // @formatter:on

        Okta.configureResourceServer401ResponseBody(http);
    }

    private void configureFlowSecurity(HttpSecurity http) throws Exception
    {
        // Not using Spring CSRF here to be able to use plain HTML for the login page
        http.csrf().disable()

                // Register our CustomRequestCache, that saves unauthorized access attempts, so
                // the user is redirected after login.
                .requestCache().requestCache(new CustomRequestCache())

                // Restrict access to our application.
                .and()
                    .authorizeRequests()

                    // Allow all flow internal requests.
                    .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

                    // (Inspired by: https://stackoverflow.com/questions/22767205/spring-security-exclude-url-patterns-in-security-annotation-configurartion)

                    // Added this one at the top.
                    // .antMatchers("/VAADIN/**").permitAll()

                    // Allow all requests by logged in users.
                    .anyRequest().authenticated()

                // Configure the login page and the failure and success pages
                .and()
                    .formLogin().loginPage(LOGIN_URL).permitAll()
                    .loginProcessingUrl(LOGIN_PROCESSING_URL)
                    .failureUrl(LOGIN_FAILURE_URL)
                    .successForwardUrl(LOGOUT_SUCCESS_FORWARD_URL)

                // Configure logout
                .and()
                    .logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
                ;
    }

    private static final String LOGIN_PROCESSING_URL = "/" + LoginView.ROUTE;
    private static final String LOGIN_FAILURE_URL = "/" + LoginView.ROUTE + "?error";
    private static final String LOGIN_URL = "/" + LoginView.ROUTE;
    private static final String LOGOUT_SUCCESS_FORWARD_URL = "/" + "server-about";

    // private static final String LOGOUT_SUCCESS_URL = "/" + LoginView.ROUTE;
    private static final String LOGOUT_SUCCESS_URL = "/";

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        log.info("At configureGlobal(..) to load the inMemoryAuthentication provider");

//      auth.authenticationProvider(au());
//      EmptyDatabase emptyDatabase = (EmptyDatabase) ApplicationContextProvider.getApplicationContext().getBean("emptyDatabase");
//      emptyDatabase.doIt();
        // auth.authenticationProvider(authenticationProvider());

        auth.inMemoryAuthentication().withUser("admin@test.dk").
                password(passwordEncoder.encode("123")).roles("ADMIN", "USER");

    }

    @Autowired
    private PasswordEncoder passwordEncoder = null;

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


}
