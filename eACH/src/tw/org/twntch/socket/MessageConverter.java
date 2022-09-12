package tw.org.twntch.socket;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.output.StringBuilderWriter;

public class MessageConverter {
	
	public static String marshalling(Object obj) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Message.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        
        StringBuilderWriter sbw = new StringBuilderWriter();
        m.marshal(obj, sbw);
        return sbw.toString();
	}
	
	public static Message unmarshalling(String xml) throws JAXBException{
		JAXBContext jc = JAXBContext.newInstance(Message.class);
        Unmarshaller u = jc.createUnmarshaller();
        
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Message msg = (Message) u.unmarshal(is);

        return msg;
	}
}
