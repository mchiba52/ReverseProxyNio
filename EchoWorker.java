import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class EchoWorker implements Runnable {
 private List<ServerDataEvent> queue = new LinkedList<ServerDataEvent>();
 
 public void processData(NioServer server, NioClient client, SocketChannel socket, byte[] data, int count) {
  byte[] dataCopy = new byte[count];
  System.arraycopy(data, 0, dataCopy, 0, count);
  synchronized(queue) {
   queue.add(new ServerDataEvent(server, client, socket, dataCopy));
   queue.notify();
  }
 }
 
 public void forwardData(NioClient client, byte[] data)
 {
     try {
         RspHandler handler = new RspHandler();
         //client.send("GET / HTTP/1.0\r\n\r\n".getBytes(), handler);
         client.send(data, handler);
         handler.waitForResponse();
     } catch (Exception e) {
         e.printStackTrace();
     }
 }
 
 public void run() {
  ServerDataEvent dataEvent;
  
  while(true) {
   // Wait for data to become available
   synchronized(queue) {
    while(queue.isEmpty()) {
     try {
      queue.wait();
     } catch (InterruptedException e) {
     }
    }
    dataEvent = queue.remove(0);
   }
   
   if (dataEvent.data.length == 0) continue;
   // Return to sender
   String str = new String(dataEvent.data);
   System.out.println("Received: " + str);
   dataEvent.server.send(dataEvent.socket, "Bye.".getBytes());
   
   // Forward to server
   if (dataEvent.client != null) {
       RPTransformT1 rpt1 = new RPTransformT1();
       RPTransformT2 rpt2 = new RPTransformT2();
       byte[] out = null;
       try {
           out = rpt1.transform(rpt2.transform(dataEvent.data));
       } catch (Exception e) {
       }
       forwardData(dataEvent.client, out);
   }
  }
 }
}
