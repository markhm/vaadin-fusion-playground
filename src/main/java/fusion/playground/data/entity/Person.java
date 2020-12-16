package fusion.playground.data.entity;


import fusion.playground.data.AbstractEntity;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import javax.annotation.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent=true)
@ToString(callSuper = true)
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
