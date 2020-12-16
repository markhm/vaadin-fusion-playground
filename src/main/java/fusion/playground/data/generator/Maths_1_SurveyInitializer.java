package fusion.playground.data.generator;

import fusion.playground.data.entity.QuestionCategory;
import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.data.service.SurveyRepository;

public class Maths_1_SurveyInitializer extends AbstractSurveyQuestionsLoader
{
    private static QuestionCategory CATEGORY_EXAMPLE = QuestionCategory.example;

    public Maths_1_SurveyInitializer(SurveyRepository surveyRepository,
                                     QuestionRepository questionRepository,
                                     PossibleAnswerRepository possibleAnswerRepository)
    {
        super(surveyRepository, questionRepository, possibleAnswerRepository);

        createSurvey("maths_1", CATEGORY_EXAMPLE.name());
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
