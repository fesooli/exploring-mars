package br.com.fellipeoliveira.exploringmars.util;

import br.com.fellipeoliveira.exploringmars.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.DirectionDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeDTO;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeDTO;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

  private final Validator validator;

  public void validate(SpaceProbeDTO spaceProbeDTO) {
    Set<ConstraintViolation<SpaceProbeDTO>> violations = validator.validate(spaceProbeDTO);
    for (ConstraintViolation<SpaceProbeDTO> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }

  public void validate(ProbeDTO probeDTO) {
    Set<ConstraintViolation<ProbeDTO>> violations = validator.validate(probeDTO);
    for (ConstraintViolation<ProbeDTO> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }

  public void validate(DirectionDTO directionDTO) {
    Set<ConstraintViolation<DirectionDTO>> violations = validator.validate(directionDTO);
    for (ConstraintViolation<DirectionDTO> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }
}
