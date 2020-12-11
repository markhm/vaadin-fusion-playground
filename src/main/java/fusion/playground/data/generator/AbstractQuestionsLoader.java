package fusion.playground.data.generator;

import fusion.playground.data.service.PossibleAnswerRepository;
import fusion.playground.data.service.QuestionRepository;
import fusion.playground.domain.PossibleAnswer;
import fusion.playground.domain.Question;

public abstract class AbstractQuestionsLoader
{
    protected String category;
    private int orderCounter = 1;

    protected QuestionRepository questionRepository;
    protected PossibleAnswerRepository possibleAnswerRepository;

    public AbstractQuestionsLoader(String category, QuestionRepository questionRepository, PossibleAnswerRepository possibleAnswerRepository)
    {
        this.category = category;
        this.questionRepository = questionRepository;
        this.possibleAnswerRepository = possibleAnswerRepository;
    }

    protected void addQuestion(String questionText, String... possibleAnswerTexts)
    {
        Question question = new Question();
        question.text(questionText);
        question.category(category);
        question.number(orderCounter);

        for (String possibleAnswerText: possibleAnswerTexts)
        {
            question.addPossibleAnswer(possibleAnswerRepository.save(new PossibleAnswer(possibleAnswerText)));
        }

        questionRepository.save(question);

        orderCounter++;
    }

    public abstract void loadQuestions();
}
