package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.domains.Location;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("L")
@RequiredArgsConstructor
public class LeftCommand implements Command {

  private final SpaceProbeGateway spaceProbeGateway;

  @Override
  public void execute(SpaceProbe spaceProbe) {
    log.info("Space Before: {}", spaceProbe);
    spaceProbe
        .getDirection()
        .setAngle(
            spaceProbe.getDirection().getAngle() == 1
                ? 4
                : (spaceProbe.getDirection().getAngle() - 1));
    spaceProbe
        .getDirection()
        .setProbeDirection(Location.findLocation(spaceProbe.getDirection().getAngle()).toString());
    spaceProbeGateway.saveProbe(spaceProbe);
    log.info("Space After: {}", spaceProbe);
  }
}
