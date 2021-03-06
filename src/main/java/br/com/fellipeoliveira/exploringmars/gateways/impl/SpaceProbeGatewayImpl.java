package br.com.fellipeoliveira.exploringmars.gateways.impl;

import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SpaceProbeGatewayImpl implements SpaceProbeGateway {

  private static Map<String, SpaceProbe> planet = new HashMap<>();

  @Override
  public List<SpaceProbe> findAllProbes() {
    return planet.keySet().stream().map(key -> planet.get(key)).collect(Collectors.toList());
  }

  @Override
  public SpaceProbe findProbeById(String id) {
    return planet.get(id);
  }

  @Override
  public void deleteProbeById(String id) {
    if(planet.get(id) == null) {
      throw new BusinessValidationException(id + ", ID not found to delete");
    }
    planet.remove(id);
  }

  @Override
  public void saveProbe(SpaceProbe spaceProbe) {
    planet.put(spaceProbe.getProbeId(), spaceProbe);
  }
}
