package br.com.fellipeoliveira.exploringmars.gateways.http.response;

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
public class ProbeResponse {

  @NotEmpty(message = "Space Probe Name can not be empty!")
  @NotNull(message = "Space Probe Name can not be null!")
  private String spaceProbeName;

}
