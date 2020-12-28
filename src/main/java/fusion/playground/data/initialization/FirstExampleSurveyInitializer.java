package fusion.playground.data.initialization;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.QuestionRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.service.SurveyService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FirstExampleSurveyInitializer extends AbstractSurveyLoader
{
    private static Log log = LogFactory.getLog(FirstExampleSurveyInitializer.class);

    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public FirstExampleSurveyInitializer(UserRepository userRepository,
                                         SurveyService surveyService,
                                         SurveyRepository surveyRepository,
                                         QuestionRepository questionRepository,
                                         PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userRepository, surveyService, surveyRepository, questionRepository, possibleAnswerRepository);

        log.info("Default owner is: "+defaultOwnerId);

        survey = surveyService.createDraftSurvey(defaultOwnerId, SurveyCategory.example.toString(), "example");

        survey.title("First example survey");
        survey.description("This is an example survey with multiple-choice questions.");
        survey.visibility(Visibility.general);
        survey.status(Survey.SurveyStatus.published);

    }

    public void loadQuestions()
    {
        addQuestion("What kind of music 🎶 do you like?",
                "Pop 🎷", "Rock 🎸", "Dance 🕺🏻", "Classical 🎻");

        addQuestion("Are you a girl or a boy?",
                "Girl 👧", "Boy 👦");

        addQuestion("What is your favourite country?",
                "Finland 🇫🇮", "Denmark 🇩🇰", "Germany 🇩🇪", "The Netherlands 🇳🇱");

        addQuestion("What is your favourite animal?",
                "Elephant 🐘", "Giraffe 🦒", "Bunny 🐇");

        addQuestion("How much would you like to eat?",
                "A little bit ▪", "Quite a bit ◼", "A gigantic amount ⬛︎");

        addQuestion("What would you like to eat?",
                "Pizza 🍕", "Pasta Bolognese 🍝", "Butter Chicken 🥘");

        addQuestion("What would you like to drink?",
                "Wine 🍷", "Limonade 🍋", "Water 🚰");

        addQuestion("What is the meaning of life?",
                "Carpe diem 🌷", "Memento mori ⚰️", "42 ⁈");
    }
}
