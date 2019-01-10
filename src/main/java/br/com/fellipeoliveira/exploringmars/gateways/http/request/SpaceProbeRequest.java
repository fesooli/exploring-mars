package br.com.fellipeoliveira.exploringmars.gateways.http.request;

import java.util.List;
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
public class SpaceProbeRequest {

  @NotEmpty(message = "Probe ID can not be empty!")
  @NotNull(message = "Probe ID can not be null!")
  private String probeId;

  @NotNull(message = "Commands can not be null!")
  private List<String> commands;

}
