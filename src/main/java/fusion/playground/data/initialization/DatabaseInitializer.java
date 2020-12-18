package fusion.playground.data.initialization;

import fusion.playground.data.service.*;
import fusion.playground.data.entity.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DatabaseInitializer
{
    private static Log log = LogFactory.getLog(DatabaseInitializer.class);

    @Bean
    @Autowired
    public InitializingBean initializeDatabase(UserRepository userRepository,
                                               SurveyRepository surveyRepository,
                                               QuestionRepository questionRepository,
                                               PossibleAnswerRepository possibleAnswerRepository,
                                               ResponseRepository responseRepository)
    {
        log.info("Initializing database");

        return () -> {

            // Start with a clean database;
            dropAllCollections(questionRepository, possibleAnswerRepository, userRepository, responseRepository);

            loadUsers(userRepository);

            loadQuestions(surveyRepository, questionRepository, possibleAnswerRepository);

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

    private void dropAllCollections(MongoRepository... mongoRepositories)
    {
        for (MongoRepository mongoRepository : mongoRepositories)
        {
            mongoRepository.deleteAll();
        }
    }

    /** Users are loaded from Okta
     *
     * @param userRepository
     */
    private static void loadUsers(UserRepository userRepository)
    {
        User regularUser = new User();
        regularUser.username("testuser");
        regularUser.emailAddress("testuser@test.com");
        regularUser.emailConfirmed(true);
        regularUser.dateOfBirth(LocalDate.now().minusYears(20));
        regularUser.oktaUserId("00u28osriV7V5f7pM5d6");

        User adminUser = new User();
        adminUser.username("admin");
        adminUser.emailAddress("admin@test.com");
        adminUser.emailConfirmed(true);
        adminUser.dateOfBirth(LocalDate.now().minusYears(40));

        List<User> userList = new ArrayList();
        userList.add(regularUser);
        userList.add(adminUser);

        userRepository.saveAll(userList);
    }

    private static void loadQuestions(SurveyRepository surveyRepository,
                                      QuestionRepository questionRepository,
                                      PossibleAnswerRepository possibleAnswerRepository)
    {
        FirstExampleSurveyInitializer exampleQuestions = new FirstExampleSurveyInitializer(surveyRepository,
                questionRepository, possibleAnswerRepository);
        exampleQuestions.loadQuestions();
        exampleQuestions.saveSurvey();

        WeatherExampleSurveyInitializer weatherExampleSurveyInitializer = new WeatherExampleSurveyInitializer(surveyRepository,
                questionRepository, possibleAnswerRepository);

        weatherExampleSurveyInitializer.loadQuestions();
        weatherExampleSurveyInitializer.saveSurvey();

        MathsExampleSurveyInitializer mathsExampleSurveyInitializer = new MathsExampleSurveyInitializer(surveyRepository,
                questionRepository, possibleAnswerRepository);
        mathsExampleSurveyInitializer.loadQuestions();
        mathsExampleSurveyInitializer.saveSurvey();
    }

    private static void printLogStatement(String statement)
    {
        log.info("******************************************");
        log.info("** " + statement + "**");
        log.info("******************************************");
        log.info("");
    }
}
