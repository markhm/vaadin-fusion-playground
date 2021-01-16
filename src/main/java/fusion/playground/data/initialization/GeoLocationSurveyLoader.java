package fusion.playground.data.initialization;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.SurveyStepRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.service.SurveyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GeoLocationSurveyLoader extends AbstractSurveyLoader
{
    private static Log log = LogFactory.getLog(GeoLocationSurveyLoader.class);

    public GeoLocationSurveyLoader(UserRepository userRepository, SurveyService surveyService,
                                   SurveyRepository surveyRepository,
                                   SurveyStepRepository surveyStepRepository,
                                   PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userRepository, surveyService, surveyRepository, surveyStepRepository, possibleAnswerRepository);

        // log.info("Default owner is: "+defaultOwnerId);

        survey = surveyService.createDraftSurvey(defaultOwnerId, SurveyCategory.example.toString(), "geoweather");

        survey.title("GeoLocation survey");
        survey.description("This is an example for a location-based survey.");
        survey.visibility(Visibility.general);
        survey.status(Survey.SurveyStatus.published);

        survey.randomizeAnswerOrder(false);
    }

    public void loadSurveySteps()
    {
        addNonQuestionStep("The goal of this survey is to understand the weather at your " +
                "current location.\n\nPlease allow this app to access your current location.");

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
