package br.com.fellipeoliveira.exploringmars.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceProbe {

  private String probeId;

  private String spaceProbeName;

  private Direction direction;

}
