package fusion.playground.security;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServerConfig
{
    @Autowired
    private Environment environment;

    private static Log log = LogFactory.getLog(ServerConfig.class);

    public ServerConfig(Environment environment)
    {
        this.environment = environment;
    }

    @Bean
    @ConditionalOnProperty(name="developmentMode", havingValue="true")
    public ServletWebServerFactory servletContainer()
    {
        TomcatServletWebServerFactory tomcat = null;

        tomcat = new TomcatServletWebServerFactory()
        {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(getHttpConnector());

        return tomcat;
    }

    private Connector getHttpConnector()
    {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(80);
        connector.setSecure(false);
        connector.setRedirectPort(443);

        return connector;
    }

}
