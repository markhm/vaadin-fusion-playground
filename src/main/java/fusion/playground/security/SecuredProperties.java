package fusion.playground.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:secured_properties.properties", ignoreResourceNotFound = true)
@ConditionalOnProperty(name="developmentMode", havingValue="true")
public class SecuredProperties
{
    @Value( "${okta_API_TOKEN}" )
    public String oktaApiToken;

    @Value( "${okta_OrgUrl}" )
    public String oktaOrgUrl;

}
