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

/**
 * Represents single peer-to-peer communication.
 * Contains data needed for sending messages: both user names, address of remote server
 * and local server port to include into message.
 * Chat history is saved and managed here.
 */
public class Chat {
  private static final Logger LOGGER = Logger.getLogger(Chat.class.getName());

  private final InetSocketAddress address;
  private final int localPort;
  @Getter
  private final ObservableList<ChatMessage> messages = FXCollections.observableArrayList();
  @Setter
  private String userName;
  @Getter
  private StringProperty friendName = new SimpleStringProperty("Unknown");

  /**
   * Creates a new chat.
   * @param address address of remote server to send messages
   * @param localPort port at which local server is running
   * @param userName name of local user
   */
  public Chat(InetSocketAddress address, int localPort, String userName) {
    this.address = address;
    this.localPort = localPort;
    this.userName = userName;
  }

  /**
   * Adds received message to chat history and updates friend name in case
   * it was changed or not initialized.
   */
  public void addReceivedMessage(ChatMessage message) {
    messages.add(message);
    friendName.set(message.getName());
  }

  /**
   * Sends message with given text through tcp-ip protocol.
   * Message is wrapped into {@link ChatMessage} object with additional information
   * needed for displaying and future communication.
   * @throws IOException if sending message failed
   */
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
