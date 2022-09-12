package tw.org.twntch.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import tw.org.twntch.util.StrUtils;

public class SocketClient {
	private String ip;
	private int port;
	private Logger log = Logger.getLogger(SocketClient.class.getName());
	
	public Map<String, String> send(String outStr){
		Map<String, String> rtnMap = null;
		SocketChannel socketChannel = null;
		outStr = outStr.trim();
		//20150109 HUANGPU 需在XML電文前加入電文總長度(4 bytes) by Fanny
		outStr = StrUtils.padZeroLeft(String.valueOf(outStr.length()), 4) + outStr;
		
		log.debug("傳送資料:\n" + outStr);
		try {
			rtnMap = new HashMap<String, String>();
			socketChannel = SocketChannel.open();
			if(socketChannel.connect(new InetSocketAddress(ip, port))){
				log.debug("已連線至 "+ip+":"+port);
				ByteBuffer buf = ByteBuffer.wrap(outStr.getBytes("UTF-8"));
				buf.clear();
				buf.put(outStr.getBytes(), 0, outStr.getBytes().length);
				buf.flip();
				log.debug("準備送出，共 "+outStr.getBytes().length+" bytes...");
				while(buf.hasRemaining()) {
					log.debug("	>>已送出 "+socketChannel.write(buf)+" bytes");
				}
				log.debug("送出成功!");
				rtnMap.put("result", "TRUE");
				rtnMap.put("msg", "已送後台處理!");
			}else{
				log.debug("送出失敗!");
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "無法連線至主機!");
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.debug("傳送失敗:" + e.toString());
			rtnMap.put("result", "FALSE");
			rtnMap.put("msg", "傳送失敗:" + e.toString());
		} finally {
			try {
				socketChannel.close();
				log.debug("isOpen>>"+socketChannel.isOpen()+",isConnected>>"+socketChannel.isConnected()+",isBlocking"+socketChannel.isBlocking()+",isConnectionPending>>"+socketChannel.isConnectionPending());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.debug("傳送失敗:" + e.toString());
				rtnMap.put("result", "FALSE");
				rtnMap.put("msg", "處理失敗:" + e.toString());
			}
		}
//		20150421 add by hugo 因使用loop重複使用此API時 普鴻的socket會有收不到資訊的情況先試試看sleep 300豪秒
		try {
			Thread.sleep(1*300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.debug("rtnMap >> " + rtnMap);
		return rtnMap;
	}
	
	//單元測試
	public static void main(String[] args) {
		SocketClient sc = new SocketClient();
		sc.setIp("10.60.1.13");
		sc.setPort(22999);
		sc.send("TEST");
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
