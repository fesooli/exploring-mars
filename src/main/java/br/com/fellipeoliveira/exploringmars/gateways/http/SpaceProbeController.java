package br.com.fellipeoliveira.exploringmars.gateways.http;

import br.com.fellipeoliveira.exploringmars.usecases.SpaceProbeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SpaceProbeController {

  private final SpaceProbeUseCase spaceProbeUseCase;

}
