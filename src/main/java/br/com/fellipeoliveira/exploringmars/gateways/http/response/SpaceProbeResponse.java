package br.com.fellipeoliveira.exploringmars.gateways.http.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceProbeResponse {

  private String probeId;

  private String spaceProbeName;

  private DirectionResponse directionResponse;

  private List<String> commandsWithError;
}
