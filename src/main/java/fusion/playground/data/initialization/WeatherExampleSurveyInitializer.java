package fusion.playground.data.initialization;

import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.SurveyRepository;

public class WeatherExampleSurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public WeatherExampleSurveyInitializer(SurveyRepository surveyRepository,
                                           QuestionRepository questionRepository,
                                           PossibleAnswerRepository possibleAnswerRepository)
    {
        super(surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey(SurveyCategory.example, "weather");

        survey.title("Weather example survey");
        survey.description("This is a short survey with multiple-choice questions about the weather.");
    }

    public void loadQuestions()
    {
        addQuestion("Is it a sunny day today?",
                "Yes", "No");

        addQuestion("Is it a raining today?",
                "Yes", "No");

        addQuestion("Is it a freezing today?",
                "Yes", "No");

        addQuestion("Are there any clouds?",
                "Yes", "No");

    }
}
