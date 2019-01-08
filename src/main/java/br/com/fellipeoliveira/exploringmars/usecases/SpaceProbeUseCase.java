package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.config.PlanetConfig;
import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.exceptions.PlanetLimitAreaValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.DirectionResponse;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.SpaceProbeResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpaceProbeUseCase {

  private final SpaceProbeGateway spaceProbeGateway;
  private final ValidationUseCase validationUseCase;
  private final PlanetConfig planetConfig;
  private final ApplicationContext context;

  public List<SpaceProbeResponse> findAllProbes() {
    return spaceProbeGateway
        .findAllProbes()
        .stream()
        .map(spaceProbe -> builderSpaceProbeResponse(spaceProbe))
        .collect(Collectors.toList());
  }

  public SpaceProbeResponse findProbeById(String id) {
    return builderSpaceProbeResponse(spaceProbeGateway.findProbeById(id));
  }

  public void saveProbe(ProbeRequest probeRequest) {
    final List<SpaceProbe> spaceProbes = spaceProbeGateway.findAllProbes();
    validationUseCase.execute(probeRequest, spaceProbes);
    final SpaceProbe spaceProbe = createSpaceProbe(probeRequest.getSpaceProbeName(), spaceProbes);
    validationUseCase.execute(spaceProbe, spaceProbes);
    saveProbe(spaceProbe);
  }

  public void updateProbeLocation(SpaceProbeRequest spaceProbeRequest) {
    spaceProbeRequest
        .getTurns()
        .forEach(turn -> ((Command) context.getBean(turn)).execute(spaceProbeRequest.getProbeId()));
  }

  private void saveProbe(SpaceProbe spaceProbe) {
    spaceProbeGateway.saveProbe(spaceProbe);
  }

  private Direction findValidProbeLocationInPlanet(List<SpaceProbe> spaceProbes) {
    int positionY = 0;
    int positionX = 0;

    for (int i = 0; i < spaceProbes.size(); i++) {
      if (positionX <= planetConfig.getMaxPositionX()) {
        if (spaceProbes.get(i).getDirection().getPositionY() == positionY) {
          if (spaceProbes.get(i).getDirection().getPositionX() == positionX) {
            positionX += planetConfig.getDistancePositions();
          } else {
            break;
          }
        }
      } else {
        throw new PlanetLimitAreaValidationException("Foi excedido o limite máximo de X.");
      }
    }

    return Direction.builder()
        .positionX(positionX)
        .positionY(positionY)
        .probeDirection("N")
        .angle(1)
        .build();
  }

  private SpaceProbe createSpaceProbe(String spaceProbeName, List<SpaceProbe> spaceProbes) {
    return SpaceProbe.builder()
        .probeId(UUID.randomUUID().toString())
        .spaceProbeName(spaceProbeName)
        .direction(findValidProbeLocationInPlanet(spaceProbes))
        .build();
  }

  private SpaceProbeResponse builderSpaceProbeResponse(SpaceProbe spaceProbe) {
    return SpaceProbeResponse.builder()
        .probeId(spaceProbe.getProbeId())
        .spaceProbeName(spaceProbe.getSpaceProbeName())
        .directionResponse(builderSpaceProbeDirectionResponse(spaceProbe.getDirection()))
        .build();
  }

  private DirectionResponse builderSpaceProbeDirectionResponse(Direction direction) {
    return DirectionResponse.builder()
        .positionX(direction.getPositionX())
        .positionY(direction.getPositionY())
        .probeDirection(direction.getProbeDirection())
        .build();
  }
}
