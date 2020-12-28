package fusion.playground.data.initialization;

import fusion.playground.data.entity.Survey;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.entity.Visibility;
import fusion.playground.data.repository.PossibleAnswerRepository;
import fusion.playground.data.repository.QuestionRepository;
import fusion.playground.data.repository.SurveyRepository;
import fusion.playground.data.repository.UserRepository;
import fusion.playground.data.service.SurveyService;

public class HarryPotterSurveyLoader extends AbstractSurveyLoader
{
    public HarryPotterSurveyLoader(UserRepository userRepository, SurveyService surveyService,
                                   SurveyRepository surveyRepository,
                                   QuestionRepository questionRepository, PossibleAnswerRepository possibleAnswerRepository)
    {
        super(userRepository, surveyService, surveyRepository, questionRepository, possibleAnswerRepository);

        survey = Survey.createDraftSurvey(hiddenOwnerId, SurveyCategory.example, "Harry Potter");

        survey.title("Harry Potter survey");
        survey.description("An survey with multiple-choice questions about the Harry Potter world.");

        survey.visibility(Visibility.personal);
    }

    @Override
    public void loadQuestions()
    {

        addQuestion("Who is your favourite character?",
                "Harry", "Hermione", "Dumbledore", "Sneep");

        addQuestion("Do you like an unfinished question?",
                "Yes", "No", "Maybe");

    }
}
