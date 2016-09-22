package ru.spbau.mit.repl;

import ru.spbau.mit.repl.shell.Shell;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    Shell shell = new Shell();
    try (Scanner scanner = new Scanner(System.in)) {
      while (scanner.hasNextLine()) {
        try {
          shell.execute(scanner.nextLine(), System.in, System.out);
        } catch (Exception e) {
          System.err.println("Failed to execute command: ");
          e.printStackTrace();
        }
      }
    }
  }
}
