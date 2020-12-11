package fusion.playground.data.service;

import fusion.playground.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer>
{
    public List<Question> findAllByCategory(String category);

    public int countAllByCategory(String category);

    public Optional<Question> getByCategoryAndNumber(String category, Integer orderNumber);
}
