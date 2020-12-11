package fusion.playground.data.generator;

import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.entity.QuestionCategory;

public class ExampleQuestionsInitializer extends AbstractQuestionsLoader
{
    private static QuestionCategory CATEGORY_EXAMPLE = QuestionCategory.example;

    public ExampleQuestionsInitializer(QuestionRepository questionRepository, PossibleAnswerRepository possibleAnswerRepository)
    {
        super(CATEGORY_EXAMPLE.name(), questionRepository, possibleAnswerRepository);
    }

    public void loadQuestions()
    {
        addQuestion("Is it a sunny day today?",
                "Yes", "No");

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
