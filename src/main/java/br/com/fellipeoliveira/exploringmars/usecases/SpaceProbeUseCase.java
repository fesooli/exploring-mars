package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.DirectionDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeDTO;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpaceProbeUseCase {

  private final SpaceProbeGateway spaceProbeGateway;

  public List<SpaceProbeDTO> findAllProbes() {
    return spaceProbeGateway
        .findAllProbes()
        .stream()
        .map(spaceProbe -> builderSpaceProbeDTO(spaceProbe))
        .collect(Collectors.toList());
  }

  public SpaceProbeDTO findProbeById(String id) {
    return builderSpaceProbeDTO(spaceProbeGateway.findProbeById(id));
  }

  public void saveProbe(SpaceProbeDTO spaceProbeDTO) {
    spaceProbeGateway.saveProbe(builderSpaceProbe(spaceProbeDTO));
  }

  public void updateProbeLocation(SpaceProbeDTO spaceProbeDTO) {
    spaceProbeGateway.updateProbeDirection(builderSpaceProbe(spaceProbeDTO));
  }

  private SpaceProbe builderSpaceProbe(SpaceProbeDTO spaceProbe) {
    return SpaceProbe.builder()
        .probeId(spaceProbe.getProbeId())
        .spaceProbeName(spaceProbe.getSpaceProbeName())
        .direction(builderSpaceProbeDirection(spaceProbe.getDirectionDTO()))
        .build();
  }

  private Direction builderSpaceProbeDirection(DirectionDTO direction) {
    return Direction.builder()
        .positionX(direction.getPositionX())
        .positionY(direction.getPositionY())
        .probeDirection(direction.getProbeDirection())
        .build();
  }

  private SpaceProbeDTO builderSpaceProbeDTO(SpaceProbe spaceProbe) {
    return SpaceProbeDTO.builder()
        .probeId(spaceProbe.getProbeId())
        .spaceProbeName(spaceProbe.getSpaceProbeName())
        .directionDTO(builderSpaceProbeDirectionDTO(spaceProbe.getDirection()))
        .build();
  }

  private DirectionDTO builderSpaceProbeDirectionDTO(Direction direction) {
    return DirectionDTO.builder()
        .positionX(direction.getPositionX())
        .positionY(direction.getPositionY())
        .probeDirection(direction.getProbeDirection())
        .build();
  }
}
