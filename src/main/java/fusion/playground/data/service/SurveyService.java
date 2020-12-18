package fusion.playground.data.service;

import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SurveyService contains the stateless services, independent of a user session.
 */
@Service
public class SurveyService extends MongoCrudService<Survey, String>
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private SurveyRepository surveyRepository;
    // private SurveySessionService surveySessionService;

    private int questionPointer = 1;

    @Autowired
    public SurveyService(SurveyRepository surveyRepository)
    {
        this.surveyRepository = surveyRepository;
    }

    @Override
    public SurveyRepository getRepository()
    {
        return surveyRepository;
    }

    public List<String> getAvailableSurveys()
    {
        List<Survey> availableSurveys = surveyRepository.findAll();

        return availableSurveys.stream().map(survey -> survey.name()).collect(Collectors.toList());
    }

//    public String beginSurvey(User user, String surveyName)
//    {
//        Survey survey = findSurveyByName(surveyName);
//        return surveyResponseService.beginSurvey(user, survey);
//    }

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
        log.info("Retrieving question #" + orderNumber + "from survey "+survey.toString());
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
