package fusion.playground.data.entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class QuestionResponse extends AbstractEntity
{
    int questionNumber;
    String questionText;
    String responseText;
    boolean correct;

}
