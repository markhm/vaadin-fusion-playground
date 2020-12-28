package fusion.playground.data.service;

import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.Question;
import fusion.playground.data.entity.Survey;
import fusion.playground.data.repository.QuestionRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;

@Service
public class QuestionService extends MongoCrudService<Question, String>
{
    private static Log log = LogFactory.getLog(QuestionService.class);

    private QuestionRepository questionRepository;
    private PossibleAnswerService possibleAnswerService;
    // private SurveyService surveyService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, PossibleAnswerService possibleAnswerService)
    {
        this.questionRepository = questionRepository;
        this.possibleAnswerService = possibleAnswerService;
//        this.surveyService = surveyService;
    }

    @Override
    protected QuestionRepository getRepository() {
        return questionRepository;
    }

    // @Cacheable("questions")
    public List<Question> findAll()
    {
        return questionRepository.findAll();
    }

    public void addPossibleAnswer(String questionId, String possibleAnswerText)
    {
        PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
        possibleAnswerService.update(possibleAnswer);

        Question question = get(questionId).get();
        question.addPossibleAnswer(possibleAnswer);
        update(question);
    }


//    @Caching(evict = {
//            @CacheEvict(value="question", allEntries=true),
//            @CacheEvict(value="questions", allEntries=true),
//            @CacheEvict(value="questions_by_category", allEntries=true)})
//    public Question save(Question question)
//    {
//        return this.questionRepository.save(question);
//    }

}
