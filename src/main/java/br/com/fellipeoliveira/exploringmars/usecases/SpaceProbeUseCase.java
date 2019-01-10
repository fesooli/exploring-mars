package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.config.PlanetConfig;
import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.exploringmars.exceptions.PositionValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.DirectionResponse;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.SpaceProbeResponse;
import java.util.ArrayList;
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

  public void saveProbe(List<ProbeRequest> probeRequests) {
    probeRequests.forEach(
        probeRequest -> {
          final List<SpaceProbe> spaceProbes = spaceProbeGateway.findAllProbes();
          validationUseCase.execute(probeRequest, spaceProbes);
          final SpaceProbe spaceProbe =
              createSpaceProbe(probeRequest.getSpaceProbeName(), spaceProbes);
          validationUseCase.execute(spaceProbe, spaceProbes);
          saveProbe(spaceProbe);
        });
  }

  public List<SpaceProbeResponse> updateProbeLocation(List<SpaceProbeRequest> spaceProbeRequests) {
    List<SpaceProbeResponse> listSpaceProbeResponse = new ArrayList<>();
    spaceProbeRequests.forEach(
        spaceProbeRequest -> {
          validationUseCase.execute(spaceProbeRequest);
          List<String> commandsWithErrors = new ArrayList<>();
          spaceProbeRequest
              .getCommands()
              .forEach(
                  command -> {
                    SpaceProbe spaceProbe =
                        spaceProbeGateway.findProbeById(spaceProbeRequest.getProbeId());
                    if (spaceProbe == null) {
                      throw new BusinessValidationException(
                          "SpaceProbeRequest object with ID "
                              + spaceProbeRequest.getProbeId()
                              + " was not found.");
                    }
                    try {
                      ((Command) context.getBean(command)).execute(spaceProbe);
                    } catch (Exception e) {
                      commandsWithErrors.add(command);
                    }
                  });
          SpaceProbeResponse spaceProbeResponse = findProbeById(spaceProbeRequest.getProbeId());
          spaceProbeResponse.setCommandsWithError(commandsWithErrors);
          listSpaceProbeResponse.add(spaceProbeResponse);
        });
    return listSpaceProbeResponse;
  }

  private void saveProbe(SpaceProbe spaceProbe) {
    spaceProbeGateway.saveProbe(spaceProbe);
  }

  private Direction findValidProbeLocationInPlanet(List<SpaceProbe> spaceProbes) {
    int positionY = 0, positionX;

    List<Integer> permitedXPositions = new ArrayList<>();
    for (int i = 0; i < planetConfig.getMaxPositionX(); i++) {
      permitedXPositions.add(i);
    }

    spaceProbes
        .stream()
        .filter(spaceProbe -> spaceProbe.getDirection().getPositionY() == positionY)
        .map(spaceProbe -> spaceProbe.getDirection().getPositionX())
        .sorted()
        .forEach(position -> permitedXPositions.remove(position));

    if (permitedXPositions.stream().findFirst().isPresent()) {
      positionX = permitedXPositions.stream().findFirst().get();
    } else {
      throw new PositionValidationException("X limit reached.");
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
