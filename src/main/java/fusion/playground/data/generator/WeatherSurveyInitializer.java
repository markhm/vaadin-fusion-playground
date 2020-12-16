package fusion.playground.data.generator;

import fusion.playground.data.entity.QuestionCategory;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.SurveyRepository;

public class WeatherSurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static QuestionCategory CATEGORY_EXAMPLE = QuestionCategory.example;

    public WeatherSurveyInitializer(SurveyRepository surveyRepository,
                                    QuestionRepository questionRepository,
                                    PossibleAnswerRepository possibleAnswerRepository)
    {
        super(surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey("weather", CATEGORY_EXAMPLE.name());
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
