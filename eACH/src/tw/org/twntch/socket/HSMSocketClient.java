package tw.org.twntch.socket;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tw.org.twntch.util.HSMMACUtils;

public class HSMSocketClient {
    protected static final Logger log = LoggerFactory.getLogger(HSMSocketClient.class);

	public static void main(String[] args) {
		try{
			
			String data = "SUIPSUIPSUIPSUIP使用上僅IPSUIPSUIP使用上僅PSUIPSUIP使用上僅是PSUIP使用上僅是應系統的週邊服務程式";
			HSMSocketClient socket = new HSMSocketClient();
			HSMSuipRequestData req = new HSMSuipRequestData();
			req.setData(data);
			
			HSMSuipResponseData resp = socket.send2HSM("10.60.5.2",8500,req);
			System.out.println(resp);
			
		}catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public static HSMSuipResponseData send2HSM(String serverIp,int port,HSMSuipRequestData data) throws Exception{
		log.info("Send to HSM Data:");
		log.info(data.toString());
		byte[] bs = data.toByteArray();
		log.info("SSSSSSS Byte Size:{}",bs.length);
		log.info("Send to HSM Data (Hex):"+HSMMACUtils.toHex(bs));
		log.info("server ip:"+serverIp);
		log.info("server port:"+port);
		log.info("connect to HSM server....");
		Socket s = new Socket(serverIp,port);
		OutputStream out = s.getOutputStream();
		log.info("Ready for send Data....");
		out.write(bs);
		out.flush();
		int a = 0x03;
		log.info("Start to Receive Data....");
		byte[] bytes = toByteArray(s.getInputStream()); 
		log.info("Receive from HSM Data(Hex):"+HSMMACUtils.toHex(bytes));
		HSMSuipResponseData resp = new HSMSuipResponseData(bytes);
		log.info("Receive from HSM Data:");
		log.info(resp.toString());
		out.close();
		s.close();
		return resp;
	}

	private static byte[] toByteArray(InputStream is ) throws Exception{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[13];

		nRead = is.read(data, 0, data.length);
		buffer.write(data, 0, nRead);

		buffer.flush();

		return buffer.toByteArray();		
	}
}
