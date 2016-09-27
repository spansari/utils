package my.samples;

import javax.jms.Session;
import javax.jms.TextMessage;

import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQQueue;
import com.ibm.mq.jms.MQQueueConnection;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import com.ibm.mq.jms.MQQueueSender;
import com.ibm.mq.jms.MQQueueSession;

public class MQClientTest {

    public static void main(String[] args) {
	try {
	    MQQueueConnectionFactory cf = new MQQueueConnectionFactory();
	    cf.setHostName("DMQWMQ01");
	    cf.setPort(50500);
	    cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
	    cf.setQueueManager("DMQWMQ01");
	    cf.setChannel("WAS.SVRCONN");

	    MQQueueConnection connection = (MQQueueConnection) cf.createQueueConnection();
	    MQQueueSession session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
	    MQQueue queue = (MQQueue) session.createQueue("TST.APP05.SOP.CUBISCAN");
	    MQQueueSender sender = (MQQueueSender) session.createSender(queue);

	    String message = "231214";

	    TextMessage textMessage = (TextMessage) session.createTextMessage(message);
	    connection.start();
	    sender.send(textMessage);

	    sender.close();
	    session.close();
	    connection.close();

	    System.out.println("Message Sent Successfully");
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
