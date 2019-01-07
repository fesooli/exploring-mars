package br.com.fellipeoliveira.exploringmars.gateways.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionDTO {

  private int positionX;

  private int positionY;

  private String probeDirection;
}
