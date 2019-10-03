package dk.kea.stud.chris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  public ServerThread(Socket socket) {
    this.socket = socket;
    try {
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    } catch (IOException e) {
      System.out.println("Error initializing IO streams.");
    }
  }

  private void disconnect() {
    try {
      in.close();
      out.close();
      socket.close();
    } catch (IOException e) {
      System.out.println("Exception on closing the IO.");
    }
  }

  @Override
  public void run() {
    String input = "";
    do {
      try {
        input = in.readLine();
        out.println(input);
      } catch (IOException e) {
        System.out.println("Connection unexpectedly interrupted. Terminating.");
        disconnect();
        return;
      }
    } while (!input.equals("."));

    disconnect();
  }
}
