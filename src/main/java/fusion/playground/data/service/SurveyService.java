package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SurveyService contains the stateless services, independent of a user session.
 */
@Service
public class SurveyService extends MongoCrudService<Survey, String>
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private UserService userService;
    private SurveyRepository surveyRepository;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository, UserService userService)
    {
        this.userService = userService;
        this.surveyRepository = surveyRepository;
    }

    @Override
    public SurveyRepository getRepository()
    {
        return surveyRepository;
    }

    public List<SurveyInfo> getAvailableSurveysForOktaUserId(String oktaUserId)
    {
        log.info("Finding user by oktaId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId);
        return getAvailableSurveys(user);
    }

    private List<SurveyInfo> getAvailableSurveys(User user)
    {
        // get all surveys with general/public visibility
        List<Survey> publicSurveys = surveyRepository.findAllByVisibility(Visibility.general);

        // find the surveys with private/personal visibility
        List<Survey> privateSurveys = surveyRepository.findAllByOwnerAndVisibility(user, Visibility.personal);

        // combine the two
        List<Survey> combinedSurveys = new ArrayList<Survey>(publicSurveys);
        combinedSurveys.addAll(privateSurveys);
        // Note: groups not implemented yet
        return combinedSurveys.stream().map(survey -> SurveyInfo.createFrom(survey)).collect(Collectors.toList());

    }

    @Deprecated
    public Survey findSurveyByName(String name)
    {
        return surveyRepository.findByName(name);
    }

    public String getSurveyDescription(String category, String name)
    {
        return surveyRepository.findByCategoryAndName(category, name).description();
    }

    // @Cacheable("questions_by_survey")
    @Deprecated
    public List<Question> findAllBySurveyName(String surveyName)
    {
        return surveyRepository.findByName(surveyName).questions();
    }

    @Deprecated
    public int countAllBySurveyName(String surveyName)
    {
        return this.findAllBySurveyName(surveyName).size();
    }


//    public Question getNextQuestion(SurveyResponse surveyResponse)
//    {
//        int lastCompletedQuestion = surveyResponse.lastCompletedQuestion();
//
//        Survey survey = surveyResponse.survey();
//        return getQuestionFromSurvey(survey, lastCompletedQuestion + 1);
//    }

//    public Question getNextQuestion(String userId, String surveyName)
//    {
//        // check if questions have already been answered today
//        // boolean alreadyComplete = answerService.answersAlreadyCompleted(ca
//        tegory, userId, LocalDate.now());
//
//        Question nextQuestion = null;
//        if (true)
//        {
//            int totalNumberOfQuestions = countAllBySurveyName(surveyName);
//            if (questionPointer > totalNumberOfQuestions)
//            {
//                questionPointer = 1;
//            }
//
//            nextQuestion = getByNameAndOrderNumber(surveyName, questionPointer);
//            questionPointer++;
//
//        }
//        else
//        {
//            nextQuestion = new Question();
//            // nextQuestion.id("-1");
//            nextQuestion.text("You already answered this category today");
//        }
//
//        return nextQuestion;
//    }

    public Question getQuestionFromSurvey(Survey survey, int orderNumber)
    {
        // log.info("Retrieving question #" + orderNumber + "from survey "+survey.toString());
        return survey.questions().get(orderNumber - 1);
    }


    // @Cacheable("question")
    @Deprecated
    private Question getByNameAndOrderNumber(String name, int orderNumber)
    {
        Survey survey = findSurveyByName(name);
        return survey.questions().get(orderNumber - 1);
    }

}
