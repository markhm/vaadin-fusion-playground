package fusion.playground.domain;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString
public class Answer extends AbstractEntity
{
    private User user;

    private Question question;

    private String answer;
}
