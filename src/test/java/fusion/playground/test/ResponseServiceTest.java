package fusion.playground.test;


import fusion.playground.data.repository.ResponseRepository;
import fusion.playground.data.service.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ResponseServiceTest
{
    @MockBean
    private static ResponseRepository responseRepository;

    @Autowired
    private ResponseService responseService;

    @MockBean
    private static UserService userService;

    @MockBean
    private static SurveyStepService surveyStepService;

    @MockBean
    private static PossibleAnswerService possibleAnswerService;

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

//        @Bean
//        public ResponseService answerService() {
//
//            // return new ResponseService(responseRepository, userService, questionService, possibleAnswerService);
//        }
    }

    @Before
    public void setUp() {

    }

    public void testSomething()
    {
    }


}
