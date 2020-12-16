package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
public class Survey extends AbstractEntity
{
    private @NonNull String name;
    private @NonNull String category;

    private  List<Question> questions;

    private boolean gradable = false;

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
