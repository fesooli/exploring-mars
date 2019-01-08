package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeDTO;
import br.com.fellipeoliveira.exploringmars.util.ValidationUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationUseCase {

  private final ValidationUtil validationUtil;

  public void execute(SpaceProbeDTO spaceProbeDTO, List<SpaceProbeDTO> spaceProbes) {
    validationUtil.validate(spaceProbeDTO);
    validationUtil.validate(spaceProbeDTO.getDirectionDTO());

    validateMaximumNumberOfProbes(spaceProbes);
    validatePosition(
        spaceProbes,
        spaceProbeDTO.getDirectionDTO().getPositionY(),
        spaceProbeDTO.getDirectionDTO().getPositionX());
  }

  public void execute(ProbeDTO probeDTO) {
    validationUtil.validate(probeDTO);
  }

  private void validateMaximumNumberOfProbes(List<SpaceProbeDTO> spaceProbes) {
    if (spaceProbes.size() == 5) {
      throw new RuntimeException("O limite máximo de sondas ao mesmo tempo é 5.");
    }
  }

  private void validatePosition(List<SpaceProbeDTO> spaceProbes, int positionY, int positionX) {
    spaceProbes
        .stream()
        .filter(
            spaceProbeDTO ->
                spaceProbeDTO.getDirectionDTO().getPositionX() == positionX
                    && spaceProbeDTO.getDirectionDTO().getPositionY() == positionY)
        .findFirst()
        .ifPresent(
            spaceProbeDTO -> {
              throw new RuntimeException("Já existe uma sonda nessa posição de Y,X.");
            });
  }
}
