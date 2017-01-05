package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import lombok.Getter;
import protocol.ChatMessage;
import server.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

public class Messenger {
  private static final Logger LOGGER = Logger.getLogger(Messenger.class.getName());

  @Getter
  private final ObservableMap<InetSocketAddress, Chat> chats = FXCollections.observableHashMap();
  private int serverPort;
  private Server server;
  private String userName;

  public void startServer(int port) throws IOException {
    serverPort = port;
    server = new Server(port);
    server.setMessageHandler(this::onMessageReceived);
  }

  public void createChat(InetSocketAddress address) throws IOException {
    if (chats.containsKey(address)) {
      LOGGER.info("Already contains chat with this address");
      return;
    }
    LOGGER.info("Creating chat: address = " + address);
    Chat chat = new Chat(address, serverPort, userName);
    chat.sendMessage(userName + " created chat");
    chats.put(address, chat);
  }

  public void setUserName(String userName) {
    this.userName = userName;
    chats.values().forEach(chat -> chat.setUserName(userName));
  }

  public void onMessageReceived(InetAddress inetAddress, ChatMessage message) {
    Platform.runLater(() -> {
      InetSocketAddress address = new InetSocketAddress(inetAddress, message.getServerPort());
      LOGGER.info("Received message from " + message.getName());
      Chat chat = chats.get(address);
      if (chat == null) {
        chat = new Chat(address, serverPort, userName);
        chats.put(address, chat);
      }
      chat.addReceivedMessage(message);
    });
  }

  public void shutdown() {
    if (server != null) {
      server.shutdown();
    }
  }
}
