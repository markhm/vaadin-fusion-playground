package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.domain.PossibleAnswer;
import fusion.playground.domain.Question;

import java.util.ArrayList;
import java.util.List;

@Endpoint
@AnonymousAllowed
public class QuestionEndpoint // extends CrudEndpoint<Question, Integer>
{
//    private QuestionService service;
//
//    public QuestionEndpoint(@Autowired QuestionService service) {
//        this.service = service;
//    }
//
//    @Override
//    protected QuestionService getService() {
//        return service;
//    }

    private static List<Question> questions = new ArrayList<>();

    private int questionPointer = 0;

    public QuestionEndpoint()
    {
        loadInitialQuestions();
    }

    public Question getNextQuestion()
    {
        Question question = questions.get(questionPointer);

        questionPointer++;
        if (questionPointer >= questions.size() - 1) {
            questionPointer = 0;
        }

        return question;
    }

    private void loadInitialQuestions()
    {
        Question firstQuestion = new Question();
        firstQuestion.id(1);
        firstQuestion.text("Is it a sunny day today...?");

        PossibleAnswer yes = new PossibleAnswer(1, "Yes");
        PossibleAnswer no = new PossibleAnswer(2, "No");

        List<PossibleAnswer> possibleAnswers_1 = new ArrayList<>();
        possibleAnswers_1.add(yes);
        possibleAnswers_1.add(no);
        firstQuestion.possibleAnswers(possibleAnswers_1);

        Question secondQuestion = new Question();
        secondQuestion.id(2);
        secondQuestion.text("What is the meaning of life...?");

        PossibleAnswer answer42 = new PossibleAnswer(3, "42");
        PossibleAnswer carpeDiem = new PossibleAnswer(4, "Carpe diem");
        PossibleAnswer mementoMori = new PossibleAnswer(5, "Memento mori");

        List<PossibleAnswer> possibleAnswers_2 = new ArrayList<>();
        possibleAnswers_2.add(answer42);
        possibleAnswers_2.add(carpeDiem);
        possibleAnswers_2.add(mementoMori);
        secondQuestion.possibleAnswers(possibleAnswers_2);

        questions.add(secondQuestion);
        questions.add(firstQuestion);

    }


}
