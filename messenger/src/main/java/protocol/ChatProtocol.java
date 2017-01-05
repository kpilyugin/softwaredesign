package protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/*
  Defines protocol for communication.
 */
public class ChatProtocol {
  public static void sendMessage(ChatMessage message, DataOutputStream output) throws IOException {
    output.writeUTF(message.getName());
    output.writeUTF(message.getMessage());
    output.writeShort(message.getServerPort());
    output.flush();
  }

  public static ChatMessage receiveMessage(DataInputStream input) throws IOException {
    String name = input.readUTF();
    String message = input.readUTF();
    int serverPort = input.readShort();
    return new ChatMessage(name, message, serverPort);
  }
}
