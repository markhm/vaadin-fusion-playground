package fusion.playground.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secured_properties.properties")
public class SecuredProperties
{
    @Value( "${okta_API_TOKEN}" )
    public String oktaApiToken;

    @Value( "${okta_OrgUrl}" )
    public String oktaOrgUrl;

}
