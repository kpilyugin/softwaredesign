package protocol;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class ChatProtocolTest {
  @Test
  public void testProtocol() throws Exception {
    ChatMessage message = new ChatMessage("1", "aaa", 80);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ChatProtocol.sendMessage(message, new DataOutputStream(output));
    ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
    ChatMessage received = ChatProtocol.receiveMessage(new DataInputStream(input));

    Assert.assertEquals(received, message);
  }
}