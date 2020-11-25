package fusion.playground;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.vaadin.artur.helpers.LaunchUtil;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the  * and some desktop browsers.
 *
 */
@SpringBootApplication
@ComponentScans({
        @ComponentScan(basePackages = "fusion.playground"),
})
@EntityScan(basePackages = {"fusion.playground.domain", "fusion.playground.data.entity"})
@PWA(name = "Playground", shortName = "Playground", iconPath = "/META-INF/resources/icons/logo.png")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator
{
    public static void main(String[] args) {
        LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}
