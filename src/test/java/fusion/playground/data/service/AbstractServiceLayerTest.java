package fusion.playground.data.service;

import fusion.playground.data.entity.User;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.service.SomeOktaUser;
import fusion.playground.test.EnableEmbeddedMongoDB;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnableEmbeddedMongoDB
public class AbstractServiceLayerTest
{
    @Autowired protected UserRepository userRepository;
    @Autowired protected UserService userService;

    @Autowired protected SurveyService surveyService;
    @Autowired protected QuestionService questionService;
    @Autowired protected PossibleAnswerService possibleAnswerService;

    @Autowired protected SurveySessionService surveySessionService;
    @Autowired protected ResponseService responseService;

    protected static User user = null;

    public AbstractServiceLayerTest()
    {}

    @BeforeEach
    public void before()
    {
        // DatabaseInitializer.loadUsers(userRepository);

        // testuser
        user = userService.findByOktaUserId(SomeOktaUser.DEFAULT_USER_OKTA_ID);
    }

}
