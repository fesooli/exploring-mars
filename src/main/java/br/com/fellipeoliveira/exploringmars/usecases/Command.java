package br.com.fellipeoliveira.exploringmars.usecases;

import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;

public interface Command {

  void execute(SpaceProbe spaceProbe);
}
