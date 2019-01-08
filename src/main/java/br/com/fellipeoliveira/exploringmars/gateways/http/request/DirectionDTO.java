package br.com.fellipeoliveira.exploringmars.gateways.http.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDTO {

  @Min(value = 0, message = "PositionX can not be less than zero!")
  @Max(value = 9, message = "PositionX can not be greater than nine!")
  private int positionX;

  @Min(value = 0, message = "PositionY can not be less than zero!")
  @Max(value = 9, message = "PositionY can not be greater than nine!")
  private int positionY;

  @NotEmpty(message = "Probe Direction can not be empty!")
  @NotNull(message = "Probe Direction can not be null!")
  private String probeDirection;
}
