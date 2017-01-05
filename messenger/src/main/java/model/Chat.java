package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;
import protocol.ChatMessage;
import protocol.ChatProtocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Chat {
  private static final Logger LOGGER = Logger.getLogger(Chat.class.getName());

  private final InetSocketAddress address;
  private final int localPort;
  @Getter
  private final ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
  @Setter
  private String userName;
  @Getter
  private StringProperty peerName = new SimpleStringProperty("Unknown");

  public Chat(InetSocketAddress address, int localPort, String userName) {
    this.address = address;
    this.localPort = localPort;
    this.userName = userName;
  }

  public void addReceivedMessage(ChatMessage message) {
    messages.add(message);
    peerName.set(message.getName());
  }

  public void sendMessage(String text) throws IOException {
    LOGGER.info("Sending message to " + address);
    ChatMessage message = new ChatMessage(userName, text, localPort);
    messages.add(message);
    try (Socket socket = new Socket()) {
      socket.connect(address, 100);
      DataOutputStream output = new DataOutputStream(socket.getOutputStream());
      ChatProtocol.sendMessage(message, output);
    }
  }
}
