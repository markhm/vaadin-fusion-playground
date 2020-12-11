package fusion.playground.data.service;

import fusion.playground.domain.PossibleAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;

@Service
public class PossibleAnswerService extends CrudService<PossibleAnswer, Integer>
{
    private PossibleAnswerRepository possibleAnswerRepository;

    public PossibleAnswerService(@Autowired PossibleAnswerRepository possibleAnswerRepository)
    {
        this.possibleAnswerRepository = possibleAnswerRepository;
    }

    public PossibleAnswerRepository getRepository()
    {
        return possibleAnswerRepository;
    }


}
