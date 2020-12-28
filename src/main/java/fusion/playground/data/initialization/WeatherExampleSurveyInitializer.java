package fusion.playground.data.initialization;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.QuestionRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.service.SurveyService;

public class WeatherExampleSurveyInitializer extends AbstractSurveyLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public WeatherExampleSurveyInitializer(UserRepository userRepository, SurveyService surveyService,
                                           SurveyRepository surveyRepository,
                                           QuestionRepository questionRepository,
                                           PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userRepository, surveyService, surveyRepository, questionRepository, possibleAnswerRepository);

        survey = surveyService.createDraftSurvey(hiddenOwnerId, SurveyCategory.example.toString(), "weather");

        survey.title("Weather example survey");
        survey.description("This is a short survey with multiple-choice questions about the weather.");

        survey.status(Survey.SurveyStatus.published);
        survey.visibility(Visibility.general);
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
