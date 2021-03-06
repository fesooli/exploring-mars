package br.com.fellipeoliveira.exploringmars.gateways.http;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
import br.com.fellipeoliveira.exploringmars.usecases.SpaceProbeUseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/probe", produces = APPLICATION_JSON_VALUE)
public class SpaceProbeController {

  private final SpaceProbeUseCase spaceProbeUseCase;

  @GetMapping
  public ResponseEntity findAllProbes() {
    log.info("RECEIVED ON FIND ALL PROBES METHOD");
    return ResponseEntity.ok().body(spaceProbeUseCase.findAllProbes());
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity findProbeById(@PathVariable(value = "id") final String id) {
    log.info("RECEIVED ON FIND PROBE BY ID METHOD");
    return ResponseEntity.ok().body(spaceProbeUseCase.findProbeById(id));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity createProbe(@RequestBody final List<ProbeRequest> probeRequest) {
    log.info("RECEIVED ON CREATE PROBE METHOD");
    spaceProbeUseCase.saveProbe(probeRequest);
    return ResponseEntity.ok().body(spaceProbeUseCase.findAllProbes());
  }

  @PutMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity updateProbe(@RequestBody final List<SpaceProbeRequest> spaceProbeRequest) {
    log.info("RECEIVED ON UPDATE PROBE METHOD");
    return ResponseEntity.ok().body(spaceProbeUseCase.updateProbeLocation(spaceProbeRequest));
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteProbe(@PathVariable(value = "id") final String id) {
    log.info("RECEIVED ON DELETE PROBE METHOD");
    spaceProbeUseCase.deleteProbeById(id);
  }
}
