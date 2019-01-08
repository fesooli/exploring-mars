package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.DirectionDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeDTO;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpaceProbeUseCase {

  private final SpaceProbeGateway spaceProbeGateway;
  private final ValidationUseCase validationUseCase;

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

  public void saveProbe(ProbeDTO probeDTO) {
    validationUseCase.execute(probeDTO);
    final List<SpaceProbeDTO> spaceProbes = findAllProbes();
    // Criar validação para a sonda só andar 1 campo por vez e não permitir teleportes

    final SpaceProbeDTO spaceProbeDTO =
        SpaceProbeDTO.builder()
            .probeId(UUID.randomUUID().toString())
            .spaceProbeName(probeDTO.getSpaceProbeName())
            .directionDTO(findValidProbeLocationInPlanet(spaceProbes))
            .build();
    spaceProbeGateway.saveProbe(builderSpaceProbe(spaceProbeDTO));
  }

  public void updateProbeLocation(SpaceProbeDTO spaceProbeDTO) {
    spaceProbeGateway.updateProbeDirection(builderSpaceProbe(spaceProbeDTO));
  }

  private DirectionDTO findValidProbeLocationInPlanet(List<SpaceProbeDTO> spaceProbes) {
    int positionY = 0;
    int positionX = 0;

    for (int i = 0; i < spaceProbes.size(); i++) {
      if (positionX <= 9) {
        if (spaceProbes.get(i).getDirectionDTO().getPositionY() == positionY) {
          if (spaceProbes.get(i).getDirectionDTO().getPositionX() == positionX) {
            positionX += 2;
          } else {
            break;
          }
        }
      } else {
        throw new RuntimeException("Foi excedido o limite máximo de X.");
      }
    }
    return DirectionDTO.builder()
        .positionX(positionX)
        .positionY(positionY)
        .probeDirection("N")
        .build();
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
