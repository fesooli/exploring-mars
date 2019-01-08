package br.com.fellipeoliveira.exploringmars.util;

import br.com.fellipeoliveira.exploringmars.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationUtil {

  private final Validator validator;

  public void validate(SpaceProbeRequest spaceProbeRequest) {
    Set<ConstraintViolation<SpaceProbeRequest>> violations = validator.validate(spaceProbeRequest);
    for (ConstraintViolation<SpaceProbeRequest> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }

  public void validate(ProbeRequest probeRequest) {
    Set<ConstraintViolation<ProbeRequest>> violations = validator.validate(probeRequest);
    for (ConstraintViolation<ProbeRequest> violation : violations) {
      throw new BusinessValidationException(violation.getMessage());
    }
  }

//  public void validate(DirectionDTO directionDTO) {
//    Set<ConstraintViolation<DirectionDTO>> violations = validator.validate(directionDTO);
//    for (ConstraintViolation<DirectionDTO> violation : violations) {
//      throw new BusinessValidationException(violation.getMessage());
//    }
//  }
}
