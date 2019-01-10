package br.com.fellipeoliveira.exploringmars.gateways.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectionResponse {

  private int positionX;

  private int positionY;

  private String probeDirection;
}
