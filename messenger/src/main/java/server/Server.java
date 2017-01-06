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

/**
 * Simple tcp-ip socket server implementation.
 * Listens to server socket at given port in separate thread, client connections
 * are handled in thread pool.
 * Each connection is used to receive a single message and closed after that.
 */
public class Server {
  private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

  private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
  private final ServerSocket serverSocket;
  @Setter
  private BiConsumer<InetAddress, ChatMessage> messageHandler;

  /**
   * Creates server socket at given port and starts main server loop which
   * listens for this socket connections.
   * @throws IOException if error occurred during socket creation
   */
  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    LOGGER.info("Started server at port " + port);
    executor.submit(this::runServerLoop);
  }

  /**
   * Accepts client connections and submits them to thread pool.
   */
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

  /**
   * Receives single message from client socket and closes connection after receiving.
   */
  private void processClient(Socket socket) {
    try {
      DataInputStream input = new DataInputStream(socket.getInputStream());
      ChatMessage message = ChatProtocol.receiveMessage(input);
      LOGGER.info("Received message from " + socket.getInetAddress());
      if (messageHandler != null) {
        messageHandler.accept(socket.getInetAddress(), message);
      }
      socket.close();
    } catch (IOException e) {
      LOGGER.warning("Failed to receive message: " + e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Closes server socket.
   */
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
