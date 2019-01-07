package br.com.fellipeoliveira.exploringmars.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Direction {

  private int positionX;

  private int positionY;

  private String direction;
}
