package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent=true)
@ToString
public class Survey extends AbstractEntity
{
    private @NonNull String name;
    private @NonNull String category;

    private  List<Question> questions;

    public void addQuestion(Question question)
    {
        if (questions == null) {
            questions = new ArrayList<>();
        }

        questions.add(question);
    }

    public Question getQuestion(int orderNumber)
    {
        return questions.get(orderNumber - 1);
    }


}
