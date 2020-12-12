package fusion.playground.data.service;

import fusion.playground.data.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService extends MongoCrudService<Question, String>
{
    @Autowired
    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository)
    {
        this.repository = repository;
    }

    @Override
    protected QuestionRepository getRepository() {
        return repository;
    }

    @Cacheable("questions")
    public List<Question> findAll()
    {
        return repository.findAll();
    }

    @Cacheable("questions_by_category")
    public List<Question> findAllByCategory(String category)
    {
        return repository.findAllByCategory(category);
    }

    @Cacheable("question")
    public Optional<Question> getByCategoryAndOrderNumber(String category, Integer number)
    {
        return repository.getByCategoryAndOrderNumber(category, number);
    }

    public int countAllByCategory(String category)
    {
        return this.findAllByCategory(category).size();
    }

    @Caching(evict = {
            @CacheEvict(value="question", allEntries=true),
            @CacheEvict(value="questions", allEntries=true),
            @CacheEvict(value="questions_by_category", allEntries=true)})
    public Question save(Question question)
    {
        return this.repository.save(question);
    }

}
