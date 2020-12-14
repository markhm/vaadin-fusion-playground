package fusion.playground.security;

import com.okta.spring.boot.oauth.Okta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// Instructions for enabling HTTPS:
// https://www.thomasvitale.com/https-spring-boot-ssl-certificate/


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
                        "/icons/**", "/images/**", "/img/**", "/frontend/**");
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // @formatter:off
        // Vaadin handles CSRF for its endpoints

        Boolean developmentMode = environment.getProperty("developmentMode", Boolean.class);

        if (developmentMode)
        {
            // All requests are required to be secure.
            http.
                    requiresChannel().
                    anyRequest().
                    requiresSecure();
        } else // -> (!developmentMode)
        {
            // // also on Heroku, things need to be secure
            http.
                    requiresChannel().
                    requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).
                    requiresSecure();
        }

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
}
