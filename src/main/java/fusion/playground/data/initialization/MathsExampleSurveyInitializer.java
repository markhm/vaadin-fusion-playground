package fusion.playground.data.initialization;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.QuestionRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.service.SurveyService;

public class MathsExampleSurveyInitializer extends AbstractSurveyLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public MathsExampleSurveyInitializer(UserRepository userRepository,
                                         SurveyService surveyService,
                                         SurveyRepository surveyRepository,
                                         QuestionRepository questionRepository,
                                         PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userRepository, surveyService, surveyRepository, questionRepository, possibleAnswerRepository);

        survey = surveyService.createDraftSurvey(defaultOwnerId, SurveyCategory.example.toString(), "maths");

        survey.title("Math example survey");
        survey.description("This is an example survey where questions have a correct answer.");

        survey.status(Survey.SurveyStatus.published);
        survey.visibility(Visibility.general);
        survey.ownerId(defaultOwnerId);
    }

    public void loadQuestions()
    {
        addFactualQuestion("What is 5 ➕  7?", 2,
                "9", "12", "14");

        addFactualQuestion("What is 17 ➖  13?", 2,
                "3", "4", "6");

        addFactualQuestion("What is 5 ✖️  13?️", 3,
                "45", "55", "65");

        addFactualQuestion("What is 20 ➗  4?", 2,
                "4", "5", "6");
    }
}
