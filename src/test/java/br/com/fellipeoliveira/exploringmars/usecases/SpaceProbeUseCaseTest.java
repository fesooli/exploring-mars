package br.com.fellipeoliveira.exploringmars.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import br.com.fellipeoliveira.exploringmars.config.PlanetConfig;
import br.com.fellipeoliveira.exploringmars.domains.Direction;
import br.com.fellipeoliveira.exploringmars.domains.SpaceProbe;
import br.com.fellipeoliveira.exploringmars.exceptions.BusinessValidationException;
import br.com.fellipeoliveira.exploringmars.gateways.SpaceProbeGateway;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.ProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.request.SpaceProbeRequest;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.DirectionResponse;
import br.com.fellipeoliveira.exploringmars.gateways.http.response.SpaceProbeResponse;
import br.com.fellipeoliveira.exploringmars.util.ValidationUtil;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

@RunWith(MockitoJUnitRunner.Silent.class)
public class SpaceProbeUseCaseTest {

  @InjectMocks private SpaceProbeUseCase spaceProbeUseCase;

  @Mock private SpaceProbeGateway spaceProbeGateway;

  @Mock private ValidationUseCase validationUseCase;

  @Mock private PlanetConfig planetConfig;

  @Mock private ApplicationContext context;

  @Captor private ArgumentCaptor<SpaceProbe> spaceProbeCaptor;

  @Test
  public void findAllProbes() {
    spaceProbeUseCase.findAllProbes();
    Mockito.verify(spaceProbeGateway, times(1)).findAllProbes();
  }

  @Test
  public void findProbeById() {
    when(spaceProbeGateway.findProbeById("id")).thenReturn(builderSpaceProbe());
    spaceProbeUseCase.findProbeById("id");
    Mockito.verify(spaceProbeGateway, times(1)).findProbeById("id");
  }

  @Test
  public void saveProbe() {
    final List<ProbeRequest> request =
        Arrays.asList(
            ProbeRequest.builder().spaceProbeName("Estrela da Morte 1").build(),
            ProbeRequest.builder().spaceProbeName("Estrela da Morte 2").build());

    when(planetConfig.getMaxPositionX()).thenReturn(2);

    spaceProbeUseCase.saveProbe(request);
    Mockito.verify(spaceProbeGateway, times(2)).saveProbe(spaceProbeCaptor.capture());
    assertThat(builderSpaceProbe("Estrela da Morte 1").getDirection())
        .isEqualTo(spaceProbeCaptor.getValue().getDirection());
  }

  @Test
  public void updateProbeLocation() {
    final List<SpaceProbeRequest> spaceProbeRequests =
        Arrays.asList(
            SpaceProbeRequest.builder().probeId("id").commands(Arrays.asList("R", "M")).build());

    when(spaceProbeGateway.findProbeById("id")).thenReturn(builderSpaceProbe());
    when(context.getBean("R")).thenReturn(new RightCommand(spaceProbeGateway));
    when(context.getBean("M")).thenReturn(new MoveCommand(spaceProbeGateway, validationUseCase));

    spaceProbeUseCase.updateProbeLocation(spaceProbeRequests);
    Mockito.verify(spaceProbeGateway, times(3)).findProbeById("id");
    Mockito.verify(spaceProbeGateway, times(2)).saveProbe(spaceProbeCaptor.capture());
    Mockito.verify(context, times(1)).getBean("R");
    Mockito.verify(context, times(1)).getBean("M");
    assertThat(spaceProbeCaptor.getValue().getDirection().getProbeDirection()).isEqualTo("E");
    assertThat(spaceProbeCaptor.getValue().getDirection().getPositionX()).isEqualTo(1);
    assertThat(spaceProbeCaptor.getValue().getDirection().getPositionY()).isEqualTo(0);
  }

  @Test(expected = BusinessValidationException.class)
  public void updateProbeLocationWithNullCommands() {
    final List<SpaceProbeRequest> spaceProbeRequests =
        Arrays.asList(SpaceProbeRequest.builder().probeId("id").build());
    when(spaceProbeGateway.findProbeById("id")).thenReturn(builderSpaceProbe());
    doThrow(new BusinessValidationException("Commands can not be null!"))
        .when(validationUseCase)
        .execute(spaceProbeRequests.get(0));

    spaceProbeUseCase.updateProbeLocation(spaceProbeRequests);
    Mockito.verify(spaceProbeGateway, times(1)).findProbeById("id");
  }

  @Test(expected = BusinessValidationException.class)
  public void updateProbeLocationWithEmptyCommands() {
    final List<SpaceProbeRequest> spaceProbeRequests =
        Arrays.asList(SpaceProbeRequest.builder().probeId("id").commands(Arrays.asList()).build());
    when(spaceProbeGateway.findProbeById("id")).thenReturn(builderSpaceProbe());
    doThrow(
            new BusinessValidationException(
                "Commands field on SpaceProbeRequest Object can not be null or empty!"))
        .when(validationUseCase)
        .execute(spaceProbeRequests.get(0));

    spaceProbeUseCase.updateProbeLocation(spaceProbeRequests);
    Mockito.verify(spaceProbeGateway, times(1)).findProbeById("id");
  }

  @Test(expected = BusinessValidationException.class)
  public void updateProbeLocationWithIdNotFound() {
    final List<SpaceProbeRequest> spaceProbeRequests =
        Arrays.asList(SpaceProbeRequest.builder().commands(Arrays.asList("R")).build());
    when(spaceProbeGateway.findProbeById("id")).thenReturn(null);
    spaceProbeUseCase.updateProbeLocation(spaceProbeRequests);
  }

  @Test
  public void updateProbeLocationWithErrorsOnCommands() {
    final List<SpaceProbeRequest> spaceProbeRequests =
        Arrays.asList(SpaceProbeRequest.builder().probeId("id").commands(Arrays.asList("R", "T")).build());

    when(spaceProbeGateway.findProbeById("id")).thenReturn(builderSpaceProbe());
    when(context.getBean("R")).thenReturn(new RightCommand(spaceProbeGateway));

    List<SpaceProbeResponse> spaceProbeResponse = spaceProbeUseCase.updateProbeLocation(spaceProbeRequests);
    assertThat(spaceProbeResponse.get(0).getCommandsWithError()).isNotNull();
  }

  private SpaceProbe builderSpaceProbe() {
    return SpaceProbe.builder()
        .probeId("id")
        .spaceProbeName("Estrela da Morte")
        .direction(
            Direction.builder().positionX(0).positionY(0).probeDirection("N").angle(1).build())
        .build();
  }

  private SpaceProbe builderSpaceProbe(String spaceName) {
    return SpaceProbe.builder()
        .probeId("id")
        .spaceProbeName(spaceName)
        .direction(
            Direction.builder().positionX(0).positionY(0).probeDirection("N").angle(1).build())
        .build();
  }

  private SpaceProbeResponse builderSpaceProbeResponse() {
    return SpaceProbeResponse.builder()
        .probeId("id")
        .spaceProbeName("Estrela da Morte")
        .directionResponse(
            DirectionResponse.builder().positionX(0).positionY(0).probeDirection("N").build())
        .build();
  }
}
