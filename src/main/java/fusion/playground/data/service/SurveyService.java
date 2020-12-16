package fusion.playground.data.service;

import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService extends MongoCrudService<Survey, String>
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private SurveyRepository surveyRepository;
    private SurveyResultService surveyResultService;

    private int questionPointer = 1;

    public SurveyService(@Autowired SurveyRepository surveyRepository,
                         @Autowired SurveyResultService surveyResultService)
    {
        this.surveyRepository = surveyRepository;
        this.surveyResultService = surveyResultService;
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

    public Survey findSurveyByName(String name)
    {
        return surveyRepository.findByName(name);
    }

    // @Cacheable("questions_by_survey")
    public List<Question> findAllBySurveyName(String surveyName)
    {
        return surveyRepository.findByName(surveyName).questions();
    }

    public int countAllBySurveyName(String surveyName)
    {
        return this.findAllBySurveyName(surveyName).size();
    }

    public Question getNextQuestion(String surveyResponseId)
    {
        Question result = null;

        SurveyResult surveyResult = surveyResultService.get(surveyResponseId).get();
        if (surveyResult.isComplete())
        {
            result = new Question();
            result.orderNumber(-1);
            result.text("Survey already completed");
        }
        else
        {
            int lastCompletedQuestion = surveyResult.lastCompletedQuestion();

            Survey survey = surveyResult.survey();
            result = getQuestionFromSurvey(survey, lastCompletedQuestion + 1);
        }
        return result;
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
//        // boolean alreadyComplete = answerService.answersAlreadyCompleted(category, userId, LocalDate.now());
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

    private Question getQuestionFromSurvey(Survey survey, int orderNumber)
    {
        return survey.questions().get(orderNumber - 1);
    }


    // @Cacheable("question")
    private Question getByNameAndOrderNumber(String name, int orderNumber)
    {
        Survey survey = findSurveyByName(name);
        return survey.questions().get(orderNumber - 1);
    }

}
