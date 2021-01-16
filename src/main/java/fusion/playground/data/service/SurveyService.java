package fusion.playground.data.service;

import fusion.playground.data.entity.*;
import fusion.playground.data.repository.SurveyRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private SurveyRepository surveyRepository;
    private SurveyStepService surveyStepService;
    private UserService userService;


    @Autowired
    public SurveyService(SurveyRepository surveyRepository, SurveyStepService surveyStepService,
                         UserService userService)
    {
        this.surveyRepository = surveyRepository;
        this.surveyStepService = surveyStepService;
        this.userService = userService;
    }

    @Override
    public SurveyRepository getRepository()
    {
        return surveyRepository;
    }

    public List<SurveyInfo> getAvailableSurveysForOktaUserId(String oktaUserId)
    {
        // log.info("Finding user by oktaId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId);
        return getAvailableSurveys(user);
    }

    public List<Survey> getOwnedSurveys(String oktaUserId)
    {
        // log.info("Finding user by oktaId: "+oktaUserId);
        User user = userService.findByOktaUserId(oktaUserId);

        return surveyRepository.findAllByOwnerId(user.id());
    }

    private List<SurveyInfo> getAvailableSurveys(User user)
    {
        // get all surveys with general/public visibility
        List<Survey> publicSurveys = surveyRepository.findAllByVisibility(Visibility.general);

        // find the surveys with private/personal visibility
        List<Survey> privateSurveys = surveyRepository.findAllByOwnerIdAndVisibility(user.id(), Visibility.personal);

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

//    // @Cacheable("questions_by_survey")
//    @Deprecated
//    public List<Question> findAllBySurveyName(String surveyName)
//    {
//        return surveyRepository.findByName(surveyName).surveySteps();
//    }

//    @Deprecated
//    public int countAllBySurveyName(String surveyName)
//    {
//        return this.findAllBySurveyName(surveyName).size();
//    }


    public void publish(String surveyId)
    {
        Survey survey = get(surveyId).get();

        survey.status(Survey.SurveyStatus.published);
        survey.visibility(Visibility.general);

        log.info("Successfully published survey "+survey.name());

        // save the survey
        update(survey);

        rebuildSurvey(surveyId);
    }

    public Survey createDraftSurvey(String ownerId, String category, String name)
    {
        Survey survey = Survey.createDraftSurvey(ownerId, SurveyCategory.createFrom(category), name);

        // save the survey, so it gets assigned a surveyId
        survey = update(survey);

        return survey;
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

    public SurveyStep getSurveyStepFromSurvey(Survey survey, int orderNumber)
    {
        // log.info("Retrieving surveyStep #" + orderNumber + "from survey "+survey.toString());
        return survey.surveySteps().get(orderNumber - 1);
    }


//    // @Cacheable("surveyStep")
//    @Deprecated
//    private SurveyStep getByNameAndOrderNumber(String name, int orderNumber)
//    {
//        Survey survey = findSurveyByName(name);
//        return survey.surveySteps().get(orderNumber - 1);
//    }

    public SurveyStep addQuestion(SurveyStep surveyStep)
    {
        log.info("addQuestion(" + surveyStep.surveyId() + ", " + surveyStep.toString()+")");
        Survey survey = get(surveyStep.surveyId()).get();

        surveyStep.questionNumber(survey.surveySteps().size() + 1);
        if (survey.status() != Survey.SurveyStatus.draft)
        {
            throw new RuntimeException("adding a surveyStep is not allowed for a Survey that is not in draft");
        }

        // explicitly set id to null, so the surveyStep gets assigned a MongoDB id.
        // (The surveyStep probably got a empty string value from the frontend.)
        if (surveyStep.id().equals(""))
        {
            surveyStep.id(null);
            surveyStepService.update(surveyStep);
        }

        // save the surveyStep, so it gets an id().
        SurveyStep savedSurveyStep = surveyStepService.update(surveyStep);

        log.info("savedSurveyStep: " + savedSurveyStep);

        // retrieve the survey, addSurvey the new surveyStep and update
        List<SurveyStep> surveySteps = survey.surveySteps();
        surveySteps.add(savedSurveyStep);

        survey.surveySteps(surveySteps);
        update(survey);

        // This is probably a bit much, but just in case
//        rebuildSurvey(survey.id());

        log.info("Question was added correctly, survey was updated and can be reloaded. ");
        return surveyStep;
    }

    /**
     * This function rebuilds a survey, so the survey document contains all the correct/updated questions.
     * @param surveyId the survey's identifier
     */
    public void rebuildSurvey(String surveyId)
    {
        Survey survey = get(surveyId).get();
        List<SurveyStep> currentSurveySteps = survey.surveySteps();

        List<SurveyStep> newQuestions = new ArrayList();
        for (SurveyStep surveyStep : currentSurveySteps)
        {
            SurveyStep newQuestion = surveyStepService.get(surveyStep.id()).get();
            newQuestions.add(newQuestion);
        }

        survey.surveySteps(newQuestions);

        update(survey);
    }

}
