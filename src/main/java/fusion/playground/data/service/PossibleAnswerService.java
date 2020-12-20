package fusion.playground.data.service;

import fusion.playground.data.entity.PossibleAnswer;
import fusion.playground.data.repository.PossibleAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.MongoCrudService;

@Service
public class PossibleAnswerService extends MongoCrudService<PossibleAnswer, String>
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
