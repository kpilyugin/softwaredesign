package server;

import org.junit.Assert;
import org.junit.Test;
import protocol.ChatMessage;
import protocol.ChatProtocol;

import java.io.DataOutputStream;
import java.net.Socket;

public class ServerTest {
  @Test
  public void testServer() throws Exception {
    int port = 10765;
    Server server = new Server(port);
    boolean[] received = {false};

    ChatMessage message = new ChatMessage("a", "a", 1);
    server.setMessageHandler((address, receivedMessage) -> {
      received[0] = true;
      Assert.assertEquals(message, receivedMessage);
    });

    try (Socket socket = new Socket("localhost", port)) {
      ChatProtocol.sendMessage(message, new DataOutputStream(socket.getOutputStream()));
    }

    Thread.sleep(100);
    Assert.assertTrue(received[0]);
  }
}