package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.config.PlanetConfig;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.exceptions.MaxNumberOfProbesValidationException;
import br.com.fellipeoliveira.exploringmars.exceptions.PositionValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
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
  private final PlanetConfig planetConfig;

  public void execute(SpaceProbeRequest spaceProbeRequest) {
    validationUtil.validate(spaceProbeRequest);
  }

  public void execute(SpaceProbe spaceProbe, List<SpaceProbe> spaceProbes) {
    validatePosition(spaceProbes, spaceProbe);
    validateLimitPositions(spaceProbe);
  }

  public void execute(ProbeRequest probeRequest, List<SpaceProbe> spaceProbes) {
    validationUtil.validate(probeRequest);
    validateMaximumNumberOfProbes(spaceProbes);
  }

  private void validateMaximumNumberOfProbes(List<SpaceProbe> spaceProbes) {
    if (spaceProbes.size() == planetConfig.getMaxNumberOfProbes()) {
      throw new MaxNumberOfProbesValidationException(
          "The maximum limit of probes at the same time is " + planetConfig.getMaxNumberOfProbes());
    }
  }

  private void validatePosition(List<SpaceProbe> spaceProbes, SpaceProbe spaceProbe) {
    spaceProbes
        .stream()
        .filter(
            spaceProbeRequest ->
                (!spaceProbeRequest.getProbeId().equalsIgnoreCase(spaceProbe.getProbeId())
                    && ((spaceProbeRequest.getDirection().getPositionX()
                            == spaceProbe.getDirection().getPositionX())
                        && (spaceProbeRequest.getDirection().getPositionY()
                            == spaceProbe.getDirection().getPositionY()))))
        .findFirst()
        .ifPresent(
            spaceProbeRequest -> {
              throw new PositionValidationException(
                  "There is already a probe at this position of X, Y.");
            });
  }

  private void validateLimitPositions(SpaceProbe spaceProbe) {
    if ((spaceProbe.getDirection().getPositionX() < 0
            || spaceProbe.getDirection().getPositionY() < 0)
        || (spaceProbe.getDirection().getPositionX() > planetConfig.getMaxPositionX()
            || spaceProbe.getDirection().getPositionY() > planetConfig.getMaxPositionY())) {
      throw new PositionValidationException(
          "The minimum limits of X and Y have been exceeded. X="
              + spaceProbe.getDirection().getPositionX()
              + ", Y="
              + spaceProbe.getDirection().getPositionY());
    }
  }
}
