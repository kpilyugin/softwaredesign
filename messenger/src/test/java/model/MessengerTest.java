package model;

import org.junit.Assert;
import org.junit.Test;
import protocol.ChatMessage;

import java.net.InetSocketAddress;

public class MessengerTest {
  @Test
  public void testSimpleChat() throws Exception {
    Messenger messenger1 = new Messenger();
    messenger1.setUserName("1");
    int port1 = 10574;
    messenger1.startServer(port1);
    InetSocketAddress address1 = new InetSocketAddress("localhost", port1);

    Messenger messenger2 = new Messenger();
    messenger2.setUserName("2");
    int port2 = 10575;
    messenger2.startServer(port2);
    InetSocketAddress address2 = new InetSocketAddress("localhost", port2);

    messenger1.createChat(address2);
    messenger1.getChats().get(address2).sendMessage("Hello");

    Thread.sleep(500);

    Chat chat = messenger2.getChats().get(address1);
    ChatMessage message = chat.getMessages().get(1);

    Assert.assertEquals("1", message.getName());
    Assert.assertEquals("Hello", message.getMessage());
    Assert.assertEquals(port1, message.getServerPort());
  }

}