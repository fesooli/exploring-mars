package br.com.fellipeoliveira.exploringmars.gateways;

import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import java.util.List;

public interface SpaceProbeGateway {

  List<SpaceProbe> findAllProbes();

  SpaceProbe findProbeById(String id);

  void deleteProbeById(String id);

  void saveProbe(SpaceProbe spaceProbe);

}
