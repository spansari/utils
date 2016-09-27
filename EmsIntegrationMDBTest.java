package com.macys.mst.wms.atlas.opt.routing.services;

import java.util.HashMap;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EmsIntegrationMDBTest {

    // @Test
    // public void testOnMessage() throws Exception {
    public static void main(String[] args) { 
	
	 System.out.println("Message ");
	Properties properties = new Properties();
	// TODO read following from properties file
	properties.put("java.naming.factory.url.pkgs", "com.tibco.tibjms.naming");
	properties.put("java.naming.factory.initial", "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
//	
	properties.put("java.naming.security.principal", "wmsuser");
	properties.put("java.naming.security.credentials", "wmsuser");
//	//properties.put("java.naming.provider.url", "tcp://tibanc04.federated.fds:7222");
	properties.put("java.naming.provider.url", "tcp://tibanp01.federated.fds:55053?remote=true");

	InitialContext context;
	try {
	    context = new InitialContext(properties);
	    // TODO read ConnectionFactory name from properties file
	    ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("WMSQueueConnFactory");
	    Connection connection = connectionFactory.createConnection("wmsuser", "wmsuser");
	    //aConnection connection = connectionFactory.createConnection();
	    // TODO read Queue Name from properties file
	    // M.D2C.WMMB.PHEME.ATLAS.DVRTSHIP.RECEIVE
	    Destination destination = (Destination) context.lookup("M.D2C.WMMB.PHEME.ATLAS.DVRTSHIP.RECEIVE");
	    //Destination destination = (Destination) context.lookup("MySampleQueue");
	    // Destination destination = (Destination)
	    // context.lookup("M.D2C.WMS.MB.ATLAS.DVRTSHIP.EVENTS");
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    MessageProducer producer = session.createProducer(destination); 

	    // TextMessage message = session.createTextMessage();
	    // message.setText("Hello from client");
	    // producer.send(message);

	    ObjectMessage objMsg = session.createObjectMessage();
	    HashMap<String, String> dvrtShip = new HashMap<String, String>();
	    dvrtShip.put("packageNumber", "00000000010366447616");
	    dvrtShip.put("actualWeight", "2.3");
	    dvrtShip.put("divertedLane", "D1");
	    objMsg.setObject(dvrtShip);
	    producer.send(objMsg);
	    System.out.println("Message Sent");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    public static void main1(String[] args) { 
	
	 System.out.println("Message ");
	Properties properties = new Properties();
	// TODO read following from properties file
	properties.put("java.naming.factory.url.pkgs", "com.tibco.tibjms.naming");
	properties.put("java.naming.factory.initial", "com.tibco.tibjms.naming.TibjmsInitialContextFactory");
//	
	properties.put("java.naming.security.principal", "wmsuser");
	properties.put("java.naming.security.credentials", "wmsuser");
//	//properties.put("java.naming.provider.url", "tcp://tibanc04.federated.fds:7222");
	properties.put("java.naming.provider.url", "tcp://tibenp10.federated.fds:7272");

	InitialContext context;
	try {
	    context = new InitialContext(properties);
	    // TODO read ConnectionFactory name from properties file
	    ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("WMBXAQueueConnectionFactory");
	    Connection connection = connectionFactory.createConnection("wmsuser", "wmsuser");
	    //aConnection connection = connectionFactory.createConnection();
	    // TODO read Queue Name from properties file
	    // M.D2C.WMMB.PHEME.ATLAS.DVRTSHIP.RECEIVE
	    Destination destination = (Destination) context.lookup("M.D2C.WMMB.PHEME.ATLAS.DVRTSHIP.RECEIVE");
	    //Destination destination = (Destination) context.lookup("MySampleQueue");
	    // Destination destination = (Destination)
	    // context.lookup("M.D2C.WMS.MB.ATLAS.DVRTSHIP.EVENTS");
	    Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    MessageProducer producer = session.createProducer(destination); 

	    // TextMessage message = session.createTextMessage();
	    // message.setText("Hello from client");
	    // producer.send(message);

	    ObjectMessage objMsg = session.createObjectMessage();
	    HashMap<String, String> dvrtShip = new HashMap<String, String>();
	    dvrtShip.put("packageNumber", "00000000010366447616");
	    dvrtShip.put("actualWeight", "2.3");
	    dvrtShip.put("divertedLane", "D1");
	    objMsg.setObject(dvrtShip);
	    producer.send(objMsg);
	    System.out.println("Message Sent");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

   }
    
    
}
