package com.bitcamp.util;

public enum Ansi {

  REDYELLOW("\033[31;43m"),
  GREEN("\033[32m"),
  BOLD("\033[1m"),
  RED("\033[31m"),
  INIT("\033[0m");

  private final String ansi;

  Ansi(String ansi) {
    this.ansi = ansi;
  }

  public String getName() {
    return ansi;
  }
}
