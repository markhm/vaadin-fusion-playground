package fusion.playground.data.generator;

import fusion.playground.data.service.ResponseRepository;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.UserRepository;
import fusion.playground.data.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseInitializer
{
    private static Log log = LogFactory.getLog(DatabaseInitializer.class);

    @Bean
    public InitializingBean initializeDatabase(@Autowired UserRepository userRepository,
                                               @Autowired QuestionRepository questionRepository,
                                               @Autowired PossibleAnswerRepository possibleAnswerRepository,
                                               @Autowired ResponseRepository responseRepository)
    {
        log.info("Initializing database");

        return () -> {

            // Start with a clean database;
            dropAllCollections(questionRepository, possibleAnswerRepository, userRepository, responseRepository);

            loadUsers(userRepository);

            loadQuestions(questionRepository, possibleAnswerRepository);

            // fetch all users
            log.info("Users found with findAll():");
            log.info("-------------------------------");

            List<User> users = userRepository.findAll();
            if (users.size() == 0)
            {
                log.info("No users found in the database. Initialization failed?");
            } else
            {
                users.forEach(user -> { log.info(user); } );
            }
            log.info("");
        };
    }

    private void dropAllCollections(QuestionRepository questionRepository,
                                    PossibleAnswerRepository possibleAnswerRepository,
                                    UserRepository userRepository,
                                    ResponseRepository responseRepository)
    {
        questionRepository.deleteAll();

        possibleAnswerRepository.deleteAll();

        userRepository.deleteAll();

        responseRepository.deleteAll();
    }


    /** Users are loaded from Okta
     *
     * @param userRepository
     */
    private static void loadUsers(UserRepository userRepository)
    {
        User regularUser = new User("John", "Doe", "testuser",
                "testuser@test.dk", "something",1);
        User adminUser = new User("Admin", "Istrator", "admin",
                "admin@test.dk", "somethingElse", 1);

        List<User> userList = new ArrayList();
        userList.add(regularUser);
        userList.add(adminUser);

        userRepository.saveAll(userList);
    }

    private static void loadQuestions(QuestionRepository questionRepository, PossibleAnswerRepository possibleAnswerRepository)
    {
        ExampleQuestionsInitializer exampleQuestions = new ExampleQuestionsInitializer(questionRepository, possibleAnswerRepository);
        exampleQuestions.loadQuestions();
    }

    private static void printLogStatement(String statement)
    {
        log.info("******************************************");
        log.info("** " + statement + "**");
        log.info("******************************************");
        log.info("");
    }
}
