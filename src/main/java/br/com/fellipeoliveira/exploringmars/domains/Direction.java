package br.com.fellipeoliveira.exploringmars.domains;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direction {

  @Min(value = 0, message = "The minimum value of positionX is 0")
  @Max(value = 9, message = "The maximum value of positionX is 9")
  private int positionX;

  @Min(value = 0, message = "The minimum value of positionY is 0")
  @Max(value = 9, message = "The maximum value of positionY is 9")
  private int positionY;

  private String probeDirection;

  @Min(value = 1, message = "The minimum value of angle is 1")
  @Max(value = 4, message = "The maximum value of angle is 4")
  private int angle;
}
