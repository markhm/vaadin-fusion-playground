package fusion.playground.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@RequiredArgsConstructor
@Accessors(fluent=true)
@MappedSuperclass
public class AbstractEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
}
