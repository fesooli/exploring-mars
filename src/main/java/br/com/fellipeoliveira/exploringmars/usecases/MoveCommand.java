package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.Location;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("M")
@RequiredArgsConstructor
public class MoveCommand implements Command {

  private final SpaceProbeGateway spaceProbeGateway;
  private final ValidationUseCase validationUseCase;

  @Override
  public void execute(String id) {
    SpaceProbe spaceProbe = spaceProbeGateway.findProbeById(id);
    SpaceProbe spaceProbeCopy =
        SpaceProbe.builder()
            .probeId(spaceProbe.getProbeId())
            .spaceProbeName(spaceProbe.getSpaceProbeName())
            .direction(
                Direction.builder()
                    .angle(spaceProbe.getDirection().getAngle())
                    .positionX(spaceProbe.getDirection().getPositionX())
                    .positionY(spaceProbe.getDirection().getPositionY())
                    .probeDirection(spaceProbe.getDirection().getProbeDirection())
                    .build())
            .build();
    log.info("Space Before: {}", spaceProbe);

    Location location = Location.findLocation(spaceProbe.getDirection().getAngle());
    if (location.getPosition() == "X") {
      if (location.getMovement() > 0) {
        spaceProbeCopy.getDirection().setPositionX(spaceProbe.getDirection().getPositionX() + 1);
      } else {
        spaceProbeCopy.getDirection().setPositionX(spaceProbe.getDirection().getPositionX() - 1);
      }
    } else {
      if (location.getMovement() > 0) {
        spaceProbeCopy.getDirection().setPositionY(spaceProbe.getDirection().getPositionY() + 1);
      } else {
        spaceProbeCopy.getDirection().setPositionY(spaceProbe.getDirection().getPositionY() - 1);
      }
    }

    validationUseCase.execute(spaceProbeCopy, spaceProbeGateway.findAllProbes());
    spaceProbeGateway.saveProbe(spaceProbeCopy);
    log.info("Space After: {}", spaceProbeCopy);
  }
}
