package fusion.playground.data.initialization;

import fusion.playground.data.entity.*;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.SurveyStepRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.service.SurveyService;
import fusion.playground.service.SomeOktaUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import java.util.Optional;

@DependsOn("databaseInitializer")
public abstract class AbstractSurveyLoader
{
    private static Log log = LogFactory.getLog(AbstractSurveyLoader.class);

    private int orderCounter = 1;

    protected UserRepository userRepository;
    protected SurveyService surveyService;
    protected SurveyRepository surveyRepository;
    protected SurveyStepRepository surveyStepRepository;
    protected PossibleAnswerRepository possibleAnswerRepository;

    protected Survey survey;
    protected String defaultOwnerId;
    protected String hiddenOwnerId;

    @Autowired
    public AbstractSurveyLoader(UserRepository userRepository, SurveyService surveyService,
                                SurveyRepository surveyRepository,
                                SurveyStepRepository surveyStepRepository,
                                PossibleAnswerRepository possibleAnswerRepository)
    {
        this.userRepository = userRepository;
        this.surveyService = surveyService;
        this.surveyRepository = surveyRepository;
        this.surveyStepRepository = surveyStepRepository;
        this.possibleAnswerRepository = possibleAnswerRepository;

        Optional<User> maybeUser = userRepository.findByOktaUserId(SomeOktaUser.VFP_ADMIN_USER_OKTA_ID);

        // if (maybeUser.isEmpty()) log.info("maybeUser is empty, which should not be possible here.");

        defaultOwnerId = maybeUser.get().id();

        Optional<User> maybeUser2 = userRepository.findByOktaUserId(SomeOktaUser.HIDDEN_USER_OKTA_ID);
        // if (maybeUser2.isEmpty()) log.info("maybeUser2 is empty, which should not be possible here.");

        hiddenOwnerId = maybeUser2.get().id();
    }

    public abstract void loadSurveySteps();

//    protected void createSurvey(SurveyCategory category, String name)
//    {
//        survey = Survey.createPublishedSurvey(category, name);
//
//        saveSurvey();
//    }
//
//    protected void createSurvey(SurveyCategory category, String name, String title)
//    {
//        survey = Survey.createPublishedSurvey(category, name);
//        survey.title(title);
//
//        saveSurvey();
//    }

    public void saveSurvey()
    {
        log.info("Saving survey: "+survey);
        surveyRepository.save(survey);
    }

    protected void addNonQuestionStep(String text)
    {
        // NB: if the survey is unsaved, we'll do that first, so a surveyStep receives the correct surveyId
        if (survey.id() == null)
        {
            saveSurvey();
        }

        SurveyStep surveyStep = new SurveyStep();
        surveyStep.type(SurveyStep.StepType.text);

        surveyStep.introduction(text);

        surveyStep.surveyId(survey.id());

        surveyStepRepository.save(surveyStep);
        survey.addSurveyStep(surveyStep);
    }

    protected void addQuestion(String questionText, String... possibleAnswerTexts)
    {
        // NB: if the survey is unsaved, we'll do that first, so a surveyStep receives the correct surveyId
        if (survey.id() == null)
        {
            saveSurvey();
        }

        SurveyStep question = new SurveyStep();
        question.type(SurveyStep.StepType.question);
        question.text(questionText);
        question.questionNumber(orderCounter);
        question.surveyId(survey.id());

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            question.addPossibleAnswer(possibleAnswerRepository.save(possibleAnswer));
        }

        surveyStepRepository.save(question);
        survey.addSurveyStep(question);

        orderCounter++;
    }

    protected void addFactualQuestion(String questionText, int correctAnswer, String... possibleAnswerTexts)
    {
        this.survey.gradable(true);

        FactualQuestion question = new FactualQuestion();
        question.text(questionText);
        question.questionNumber(orderCounter);

        int counter = 1;
        for (String possibleAnswerText: possibleAnswerTexts)
        {
            PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
            question.addPossibleAnswer(possibleAnswerRepository.save(possibleAnswer));

            if (counter == correctAnswer)
            {
                question.correctAnswer(possibleAnswer);
            }
            counter++;
        }

        surveyStepRepository.save(question);
        survey.addSurveyStep(question);

        orderCounter++;
    }
}
