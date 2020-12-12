package fusion.playground.data.service;

import fusion.playground.data.endpoint.SurveyEndpoint;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyService
{
    private static Log log = LogFactory.getLog(SurveyService.class);

    private SurveyRepository surveyRepository;
    private QuestionService questionService;

    private int questionPointer = 1;

    public SurveyService(@Autowired SurveyRepository surveyRepository,
                         @Autowired QuestionService questionService)
    {
        this.surveyRepository = surveyRepository;
    }

    public List<String> getAvailableSurveys()
    {
        List<Survey> availableSurveys = surveyRepository.findAll();

        return availableSurveys.stream().map(survey -> survey.name()).collect(Collectors.toList());
    }

    public Survey findSurveyByName(String name)
    {
        return surveyRepository.findByName(name);
    }

    @Cacheable("questions_by_survey")
    public List<Question> findAllBySurveyName(String surveyName)
    {
        return surveyRepository.findByName(surveyName).questions();
    }

    public int countAllBySurveyName(String surveyName)
    {
        return this.findAllBySurveyName(surveyName).size();
    }

    public Question getNextQuestion(String userId, String surveyName)
    {
        // check if questions have already been answered today
        // boolean alreadyComplete = answerService.answersAlreadyCompleted(category, userId, LocalDate.now());

        Question nextQuestion = null;
        if (true)
        {
            int totalNumberOfQuestions = countAllBySurveyName(surveyName);
            if (questionPointer > totalNumberOfQuestions)
            {
                questionPointer = 1;
            }

            nextQuestion = getByNameAndOrderNumber(surveyName, questionPointer);
            questionPointer++;

        }
        else
        {
            nextQuestion = new Question();
            // nextQuestion.id("-1");
            nextQuestion.text("You already answered this category today");
        }

        return nextQuestion;
    }

    @Cacheable("question")
    public Question getByNameAndOrderNumber(String name, int orderNumber)
    {
        Survey survey = findSurveyByName(name);
        return survey.questions().get(orderNumber - 1);
    }

}
