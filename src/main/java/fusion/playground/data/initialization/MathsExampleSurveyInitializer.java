package fusion.playground.data.initialization;

import fusion.playground.data.entity.SurveyCategory;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.SurveyRepository;

public class MathsExampleSurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static SurveyCategory CATEGORY_EXAMPLE = SurveyCategory.example;

    public MathsExampleSurveyInitializer(SurveyRepository surveyRepository,
                                         QuestionRepository questionRepository,
                                         PossibleAnswerRepository possibleAnswerRepository)
    {
        super(surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey(SurveyCategory.example, "maths");

        survey.title("Math example survey");
        survey.description("This is an example survey where questions have a correct answer.");
    }

    public void loadQuestions()
    {
        addFactualQuestion("What is 5 + 7?", 2,
                "9", "12", "14");

        addFactualQuestion("What is 17 - 13?", 2,
                "3", "4", "6");

        addFactualQuestion("What is 5 x 13?", 3,
                "45", "55", "65");

        addFactualQuestion("What is 20 / 4?", 2,
                "4", "5", "6");
    }
}
