package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Defines protocol for sending and receiving chat messages.
 **/
public class ChatProtocol {
  /**
   * Writes message object to {@link DataOutputStream}.
   * Each message contains text of message, name of sender and local port at which
   * server is running for following communication.
   **/
  public static void sendMessage(ChatMessage message, DataOutputStream output) throws IOException {
    output.writeUTF(message.getName());
    output.writeUTF(message.getMessage());
    output.writeShort(message.getServerPort());
  }

  /**
   * Reads message object from {@link DataInputStream}.
   * @throws IOException if error occurred during reading data from stream
   */
  public static ChatMessage receiveMessage(DataInputStream input) throws IOException {
    String name = input.readUTF();
    String message = input.readUTF();
    int serverPort = input.readShort();
    return new ChatMessage(name, message, serverPort);
  }
}
