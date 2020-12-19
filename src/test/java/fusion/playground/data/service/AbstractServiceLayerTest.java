package fusion.playground.data.service;

import fusion.playground.data.entity.User;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractServiceLayerTest
{
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
        // testuser
        user = userService.findByOktaUserId("00u28osriV7V5f7pM5d6");
    }

}
