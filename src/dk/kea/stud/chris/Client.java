package dk.kea.stud.chris;

import java.util.Scanner;

public class Client {
  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);
    System.out.print("Enter address [def 127.0.0.1]: ");

    String address = scn.nextLine();
    if (address.equals("") || !address.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")) {
      address = "127.0.0.1";
    }

    System.out.print("Enter port [def 12345]: ");
    int port;
    String input = scn.nextLine();

    if (input.equals("")) {
      port = 12345;
    } else {
      try {
        port = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("Invalid input; using port 12345");
        port = 12345;
      }
    }

    int noConnections = 0;
    do {
      System.out.print("How many connections should we try to open? [>0]");
      input = scn.nextLine();
      try {
        noConnections = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("That's not a number, try again;");
      }
    } while (noConnections < 1);

    int noMessages = 0;
    do {
      System.out.print("How many messages should be sent on each connection? [>0]");
      input = scn.nextLine();
      try {
        noMessages = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("That's not a number, try again;");
      }
    } while (noMessages < 1);

    int interval = -1;
    do {
      System.out.print("How much time to wait between messages, in milliseconds? [>=0]");
      input = scn.nextLine();
      try {
        interval = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("That's not a number, try again;");
      }
    } while (interval < 0);

    System.out.println("Here we go...");
    for (int i = 0; i < noConnections; i++) {
      new Thread(new ClientThread(address, port, noMessages, interval)).start();
    }
    System.out.println("Done spawning all threads.");
  }
}
