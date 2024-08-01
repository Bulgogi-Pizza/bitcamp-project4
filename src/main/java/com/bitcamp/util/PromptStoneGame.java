package com.bitcamp.util;

public class PromptStoneGame extends Prompt {

  Print print = new Print();

  public int inputInt(String format, Object... args) {
    while (true) {
      try {
        return Integer.parseInt(input(format, args));
      } catch (NumberFormatException e) {
        System.out.println("[System] 숫자로 입력해 주세요.");
      }
    }
  }

  public int inputIntWithRange(int min, int max, String format, Object... args) {
    while (true) {
      int inputNum = inputInt(format, args);

      if (min <= inputNum && inputNum <= max) {
        return inputNum;
      } else {
        System.out.println("[System] 올바른 항목을 입력해 주세요.");
      }
    }
  }
}
