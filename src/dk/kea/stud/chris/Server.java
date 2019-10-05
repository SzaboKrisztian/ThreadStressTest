package dk.kea.stud.chris;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

  public static void main(String[] args) {
    Scanner scn = new Scanner(System.in);

    String threadMode;
    do {
      System.out.print("Select: [1] Naive threads mode [2] Thread pool mode");
      threadMode = scn.nextLine();
    } while (!threadMode.equals("1") && !threadMode.equals("2"));


    String input;
    int noThreads = 0;
    if (threadMode.equals("2")) {
      do {
        System.out.println("Maximum number of threads? [>0]");
        input = scn.nextLine();
        try {
          noThreads = Integer.parseInt(input);
        } catch (NumberFormatException e) {
          System.out.println("That was not a number. Try again.");
        }
      } while (noThreads < 1);
    }

    System.out.print("Enter port [def 12345]: ");
    int port;
    input = scn.nextLine();
    if (input.equals("")) {
      port = 12345;
    } else {
      try {
        port = Integer.parseInt(input);
        if (port < 0 || port > 65535) {
          System.out.println("Invalid input; using port 12345");
          port = 12345;
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input; using port 12345");
        port = 12345;
      }
    }

    ServerSocket serverSocket = null;
    try {
      serverSocket = new ServerSocket(port);
    } catch (IOException e) {
      System.out.println("Failed binding to port " + port + ". Aborting.");
      System.exit(1);
    }

    Socket newConnection = null;
    System.out.println("Waiting for connections.");
    if (threadMode.equals("1")) {
      while (true) {
        try {
          newConnection = serverSocket.accept();
        } catch (IOException e) {
          System.out.println("Error establishing connection.");
        }
        if (newConnection != null) {
          new Thread(new ServerThread(newConnection)).start();
        }
      }
    } else {
      ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(noThreads);
      while (true) {
        try {
          newConnection = serverSocket.accept();
        } catch (IOException e) {
          System.out.println("Error establishing connection.");
        }
        if (newConnection != null) {
          executor.execute(new ServerThread(newConnection));
        }
      }
    }
  }

}