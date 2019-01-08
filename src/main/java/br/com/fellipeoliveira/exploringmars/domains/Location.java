package br.com.fellipeoliveira.exploringmars.domains;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

public enum Location {
  N(1, "Y", 1),
  E(2, "X", 1),
  S(3, "Y", -1),
  W(4, "X", -1);

  Location(Integer location, String position, Integer moviment) {
    this.location = location;
    this.position = position;
    this.movement = moviment;
  }

  @Getter private Integer location;
  @Getter private String position;
  @Getter private Integer movement;

  public static Location findLocation(int location) {
    return Arrays.stream(values())
        .filter(locations -> locations.getLocation().equals(location))
        .findFirst().get();
  }
}
