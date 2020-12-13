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

        // All requests are required to be secure.

        // also on Heroku, things need to be secure
        Boolean developmentMode = environment.getProperty("developmentMode", Boolean.class);
        log.info("developmentMode: " + developmentMode);

        if (!developmentMode)
        {
            http.
                    requiresChannel().
                    requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).
                    requiresSecure();
        }
// http.headers().httpStrictTransportSecurity().disable();

        http.csrf().ignoringAntMatchers("/connect/**")
                .and()
                .authorizeRequests()
                // allow access to everything, Vaadin will handle security
                .antMatchers("/**").permitAll()
                .and()
                .oauth2ResourceServer().jwt();
        // @formatter:on

        if (developmentMode)
        {
            // Is there a problem here...?
            http.
                    requiresChannel().
                    anyRequest().
                    requiresSecure();
        }
        Okta.configureResourceServer401ResponseBody(http);
    }
}


//
// This is for when deployed to the cloud. See Matt Raible's presentation.
//        http.requiresChannel()
//                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
//                .requiresSecure();

// Allow all flow internal requests.
// http.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll();


//        String serverHttpPort = environment.getProperty("server.http.port");
//        log.info("serverHttpPort: " + serverHttpPort);
//
//        String serverPort = environment.getProperty("server.port");
//        log.info("serverPort: " + serverPort);


//    /**
//     * Allows access to static resources, bypassing Spring security.
//     */
//    @Override
//    public void configure(WebSecurity web) // throws Exception
//    {
//        web.ignoring().antMatchers(
////                // Vaadin Flow static resources
////                "/VAADIN/**",
//
//                // the standard favicon URI
//                "/favicon.ico",
//                // the robots exclusion standard
//                "/robots.txt",
//                // web application manifest
//                "/manifest.webmanifest",
//                "/sw.js",
//                "/offline-page.html",
//                // icons and images
//                "/icons/**",
//                "/images/**",
//                "/img/**",
//                // (development mode) static resources
//                "/frontend/**",
//                // (development mode) webjars
//                "/webjars/**",
//                // (development mode) H2 debugging console
//                "/h2-console/**",
//                // (production mode) static resources
//                "/frontend-es5/**", "/frontend-es6/**"
//
////                 ,"/**"
////                   ,"/"
//
////                 ,"/"
//        );  // to ensure people can read the introduction view without logging in
//    }
