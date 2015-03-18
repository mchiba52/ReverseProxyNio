import java.nio.channels.SocketChannel;

class ServerDataEvent {
 public NioServer server;
 public NioClient client;
 public SocketChannel socket;
 public byte[] data;
 
 public ServerDataEvent(NioServer server, NioClient client, SocketChannel socket, byte[] data) {
  this.server = server;
  this.client = client;
  this.socket = socket;
  this.data = data;
 }
}