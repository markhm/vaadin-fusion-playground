package fusion.playground.domain;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Getter
@Setter
// @RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Entity
public class Question implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @EqualsAndHashCode.Exclude
    private String text;

    @EqualsAndHashCode.Exclude
    @OneToMany
    private List<PossibleAnswer> possibleAnswers;
}
