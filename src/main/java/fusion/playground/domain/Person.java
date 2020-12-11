package fusion.playground.domain;

import javax.persistence.Entity;

import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.time.LocalDate;
import javax.annotation.Nullable;

@Getter
@Setter
@NoArgsConstructor
@Accessors(fluent=true)
@EqualsAndHashCode
@ToString
@Entity
@Table(name= "person")
public class Person extends AbstractEntity
{
  private String firstName;
  private String lastName;
  @Email
  private String email;
  private String phone;
  @Nullable
  private LocalDate dateOfBirth;
  private String occupation;

}
