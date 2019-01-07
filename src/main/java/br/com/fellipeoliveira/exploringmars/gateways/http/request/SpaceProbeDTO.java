package br.com.fellipeoliveira.exploringmars.gateways.http.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceProbeDTO {

  private String probeId;

  private String spaceProbeName;

  private DirectionDTO directionDTO;

}
