package br.com.fellipeoliveira.exploringmars.domains;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

public enum Commands {
  L("L"),
  R("R"),
  M("M");

  Commands(String command) {
    this.command = command;
  }

  @Getter private String command;

  public static Optional<Commands> findCommand(String command) {
    return Arrays.stream(values())
        .filter(commands -> commands.getCommand().equals(command))
        .findFirst();
  }
}
