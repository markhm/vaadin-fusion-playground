package fusion.playground.test;

import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// https://stackoverflow.com/questions/52604062/how-to-disable-flapdoodle-embedded-mongodb-in-certain-tests

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EmbeddedMongoAutoConfiguration.class)
public @interface EnableEmbeddedMongoDB {
}
