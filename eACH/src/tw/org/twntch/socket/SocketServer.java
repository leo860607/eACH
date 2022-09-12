package tw.org.twntch.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class SocketServer implements Runnable{
	private String ip = "127.0.0.1";
	private int port = 9090;
	ServerSocketChannel serverSocketChannel = null;
	
	public void run(){
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(ip, port));
			serverSocketChannel.configureBlocking(false);
			System.out.println("等待連線中("+ip+":"+port+")...");
			while(true){
			    SocketChannel socketChannel = serverSocketChannel.accept();
			    if(socketChannel != null){
			    	System.out.println("=========================================================");
			    	System.out.println("=========================================================");
			    	System.out.println("收到訊息，處理中...");
			    	ByteBuffer buf = ByteBuffer.allocate(1024);
			    	int bytesRead = 0;
			    	byte data[] = null;
			    	String msg_UTF8 = "";
			    	while((bytesRead = socketChannel.read(buf)) > 0){
			    		System.out.println("	>>已收取 "+bytesRead+" bytes");
			    		data = buf.array();
			    		//msg += byteBuffferToString(buf);
			    		msg_UTF8 += new String(data, "UTF-8");
			    	}
			    	System.out.println("訊息內容(UTF-8)：\n" + msg_UTF8);
			    	System.out.println("訊息內容(BIG5)：\n" + java.net.URLDecoder.decode(msg_UTF8, "UTF-8"));
			    	socketChannel.close();
			    	System.out.println("=========================================================");
			    	System.out.println("=========================================================");
			    }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String byteBuffferToString(ByteBuffer buffer){
		CharBuffer charBuffer = null;
		try {
			Charset charset = Charset.forName("BIG5");
			CharsetDecoder decoder = charset.newDecoder();
			charBuffer = decoder.decode(buffer);
			buffer.flip();
			return charBuffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		new Thread(new SocketServer()).start();
	}
}
