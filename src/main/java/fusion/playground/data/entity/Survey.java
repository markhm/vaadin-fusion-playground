package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Survey extends AbstractEntity
{
    private @NonNull String category;
    private @NonNull String name;

    private String title;
    private String description = "There is no description available yet for this survey.";

    private List<Question> questions;

    private boolean gradable = false;

    public Survey(SurveyCategory category, String name)
    {
        this.category = category.toString();
        this.name = name;
    }

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
