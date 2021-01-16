package fusion.playground.data.service;

import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.entity.SurveyStep;
import fusion.playground.data.repository.SurveyStepRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

@Service
public class SurveyStepService extends MongoCrudService<SurveyStep, String>
{
    private static Log log = LogFactory.getLog(SurveyStepService.class);

    private SurveyStepRepository surveyStepRepository;
    private PossibleAnswerService possibleAnswerService;
    // private SurveyService surveyService;

    @Autowired
    public SurveyStepService(SurveyStepRepository surveyStepRepository, PossibleAnswerService possibleAnswerService)
    {
        this.surveyStepRepository = surveyStepRepository;
        this.possibleAnswerService = possibleAnswerService;
//        this.surveyService = surveyService;
    }

    @Override
    protected SurveyStepRepository getRepository() {
        return surveyStepRepository;
    }

//    // @Cacheable("questions")
//    public List<Question> findAll()
//    {
//        return questionRepository.findAll();
//    }

    public void addPossibleAnswer(String questionId, String possibleAnswerText)
    {
        PossibleAnswer possibleAnswer = new PossibleAnswer(possibleAnswerText);
        possibleAnswerService.update(possibleAnswer);

        SurveyStep question = get(questionId).get();
        question.addPossibleAnswer(possibleAnswer);
        update(question);
    }


//    @Caching(evict = {
//            @CacheEvict(value="surveyStep", allEntries=true),
//            @CacheEvict(value="questions", allEntries=true),
//            @CacheEvict(value="questions_by_category", allEntries=true)})
//    public Question save(Question surveyStep)
//    {
//        return this.questionRepository.save(surveyStep);
//    }

}
