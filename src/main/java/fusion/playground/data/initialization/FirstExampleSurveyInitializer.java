package fusion.playground.data.initialization;

import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.service.SurveyRepository;

public class FirstExampleSurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public FirstExampleSurveyInitializer(SurveyRepository surveyRepository,
                                         QuestionRepository questionRepository,
                                         PossibleAnswerRepository possibleAnswerRepository)
    {
        super(surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey(SurveyCategory.example, "example");

        survey.title("First example survey");
        survey.description("This is an example survey with multiple-choice questions.");
    }

    public void loadQuestions()
    {
        addQuestion("What kind of music do you like?",
                "Pop", "Rock", "Dance");

        addQuestion("Are you a girl or a boy?",
                "Girl", "Boy");

        addQuestion("What is your favourite country?",
                "Finland", "Denmark", "Germany", "The Netherlands");

        addQuestion("What is your favourite animal?",
                "Elephant", "Giraffe", "Bunny");

        addQuestion("How much would you like to eat?",
                "A little bit", "Quite a bit", "A gigantic amount");

        addQuestion("What would you like to eat?",
                "Pizza", "Pasta Carbonara", "Butter Chicken");

        addQuestion("What would you like to drink?",
                "Wine", "Limonade", "Water");

        addQuestion("What is the meaning of life?",
                "Carpe diem", "Memento mori", "42");
    }

}
