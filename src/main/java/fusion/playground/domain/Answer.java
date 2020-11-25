package fusion.playground.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Entity
public class Answer implements Serializable
{
    @Id
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne
    private Question question;

    private int answer;
}
