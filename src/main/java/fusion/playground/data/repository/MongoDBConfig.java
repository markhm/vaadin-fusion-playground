package fusion.playground.data.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;


// https://reflectoring.io/spring-boot-conditionals/

@Configuration
@ConditionalOnProperty(value="system.environment", havingValue = "heroku", matchIfMissing = false)
class MongoDBConfig extends AbstractMongoClientConfiguration
{
    @Value("${spring.data.mongodb.uri}")
    public String mongoUri;

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.applyConnectionString(new ConnectionString(mongoUri));
    }

    @Override
    protected String getDatabaseName() {
        return "vaadin-fusion-playground";
    }
}
