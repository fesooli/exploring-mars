package br.com.fellipeoliveira.exploringmars.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "planet")
public class PlanetConfig {

  private int maxNumberOfProbes;
  private int maxPositionX;
  private int maxPositionY;
}
