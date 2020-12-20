package fusion.playground.data.service;

import fusion.playground.data.entity.Question;
import fusion.playground.data.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;

@Service
public class QuestionService extends MongoCrudService<Question, String>
{
    private QuestionRepository questionRepository;
    // private SurveyService surveyService = null;

    @Autowired
    public QuestionService(QuestionRepository questionRepository)
    {
        this.questionRepository = questionRepository;
        // this.surveyService = surveyService;
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

//    @Caching(evict = {
//            @CacheEvict(value="question", allEntries=true),
//            @CacheEvict(value="questions", allEntries=true),
//            @CacheEvict(value="questions_by_category", allEntries=true)})
//    public Question save(Question question)
//    {
//        return this.questionRepository.save(question);
//    }

}
