package server;

import lombok.Setter;
import protocol.ChatMessage;
import protocol.ChatProtocol;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class Server {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
  private final ServerSocket serverSocket;
  @Setter
  private BiConsumer<InetAddress, ChatMessage> messageHandler;

  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    LOGGER.info("Started server at port " + port);
    executor.submit(this::runServerLoop);
  }

  private void runServerLoop() {
    while (!serverSocket.isClosed()) {
      try {
        Socket socket = serverSocket.accept();
        executor.submit(() -> processClient(socket));
      } catch (SocketException e) {
        LOGGER.info("Socket is closed");
      } catch (IOException e) {
        LOGGER.warning("Connection to client failed: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }

  private void processClient(Socket socket) {
    try {
      DataInputStream input = new DataInputStream(socket.getInputStream());
      ChatMessage message = ChatProtocol.receiveMessage(input);
      if (messageHandler != null) {
        messageHandler.accept(socket.getInetAddress(), message);
        LOGGER.fine("Received message from " + socket.getInetAddress());
      }
      socket.close();
    } catch (IOException e) {
      LOGGER.warning("Failed to receive message: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void shutdown() {
    LOGGER.info("Shutdown server");
    try {
      serverSocket.close();
    } catch (IOException e) {
      LOGGER.warning("Failed to close server socket");
      e.printStackTrace();
    }
  }
}
