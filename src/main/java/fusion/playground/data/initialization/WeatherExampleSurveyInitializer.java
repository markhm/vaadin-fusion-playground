package fusion.playground.data.initialization;

import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.entity.User;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.service.*;

public class WeatherExampleSurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public WeatherExampleSurveyInitializer(UserService userService, SurveyRepository surveyRepository,
                                           QuestionRepository questionRepository,
                                           PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userService, surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey(SurveyCategory.example, "weather");

        survey.title("Weather example survey");
        survey.description("This is a short survey with multiple-choice questions about the weather.");

        survey.visibility(Visibility.general);
        survey.owner(defaultOwner);
    }

    public void loadQuestions()
    {
        addQuestion("Is it a sunny day today? ☀️",
                "Yes", "No");

        addQuestion("Are there any clouds? ☁️",
                "Yes", "No");

        addQuestion("Will it rain today? 🌧",
                "Yes", "No");

        addQuestion("Is it freezing today? 🥶",
                "Yes", "No");

        addQuestion("Has there been a thunderstorm in the past month? ⛈️",
                "Yes", "No");

        addQuestion("Have you ever seen a rainbow? 🌈️",
                "Yes", "No");
    }
}
