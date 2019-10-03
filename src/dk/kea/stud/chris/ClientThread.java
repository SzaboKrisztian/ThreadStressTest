package dk.kea.stud.chris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;
  private int noMessages;
  private int interval;

  public ClientThread(String host, int port, int noMessages, int interval) {
    try {
      this.socket = new Socket(host, port);
      try {
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      } catch (IOException e) {
        System.out.println("Error initializing IO streams.");
      }
      this.noMessages = noMessages;
      this.interval = interval;
    } catch (IOException e) {
      System.out.println("Error connecting to server.");
    }
  }

  @Override
  public void run() {
    if (socket != null) {
      for (int i = 0; i < noMessages; i++) {
        try {
          out.println("SPAM SPAM SPAM");
          in.readLine();
        } catch (IOException e) {
          System.out.println("Error receiving server reply.");
        }
        try {
          Thread.sleep(interval);
        } catch (InterruptedException e) {
          System.out.println(Thread.currentThread().getName() + ", interrupted.");
        }
      }
      try {
        out.println(".");
        in.readLine();
      } catch (IOException e) {
        System.out.println("Error receiving server reply.");
      }
      try {
        out.close();
        in.close();
        socket.close();
      } catch (IOException e) {
        System.out.println("Exception on closing the IO.");
      }
      System.out.println(Thread.currentThread().getName() + " finished job successfully.");
    } else {
      System.out.println(Thread.currentThread().getName() + " ended prematurely.");
    }
  }
}