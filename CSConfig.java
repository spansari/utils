
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.connectship.ampcore.AMPServices;
import com.connectship.ampcore.CoreXmlPort;

public enum CSConfig {
    instance;
    static Logger log = LoggerFactory.getLogger(CSConfig.class);
    private static final String CS_CONFIG = "csi.properties";
    private static final String PROPERTIES_DIR_KEY = "propsLocation";
    public static final String AMP_URL = "cs.amp.service.url";
    public static final String AMP_CLOSEOUT_URL = "cs.amp.closeout.url";
    public static final String AMP_CLOSEOUT_TIMEOUT = "cs.amp.closeout.timeout.minutes";
    public static final String CS_SERVER_COUNT = "cs.server.count";
    private Properties propFile;

    private transient AMPServices amp;
    private transient CoreXmlPort closeoutPort;
    private transient AMPServices closeoutAmp;
    private transient Mapper mapper = null;

    private static Map<String, CoreXmlPort> portMapping = new HashMap<>();

    public String get(String key) {
	if (propFile == null) {
	    init();
	}
	return this.propFile.getProperty(key);
    }

    private void init() {
	propFile = new java.util.Properties();
	init(CS_CONFIG, propFile);

	List<String> list = new ArrayList<>();
	list.add("mapping.xml");
	mapper = new DozerBeanMapper(list);

	initAmpService();
    }

    private void initAmpService() {

	try {
	    if (null == amp) {
		amp = new AMPServices(new URL(get(AMP_URL)));
	    }
	    if (null == closeoutPort) {
		closeoutAmp = new AMPServices(new URL(get(AMP_CLOSEOUT_URL)));
		closeoutPort = closeoutAmp.getAMPSoapService();
		String timeoutStr = get(AMP_CLOSEOUT_TIMEOUT);
		if (null == timeoutStr || timeoutStr.length() == 0) {
		    timeoutStr = "5"; // Default timeout is 5 mins
		}

		((BindingProvider) closeoutPort).getRequestContext().put("com.sun.xml.ws.request.timeout", Integer.valueOf(timeoutStr) * 60 * 1000);

	    }
	} catch (MalformedURLException e) {
	    log.error("AMP Initialization Error:" + e);
	}
    }

    private void init(String propFileName, Properties propFile) {
	try {
	    File initialFile = new File(System.getProperty(PROPERTIES_DIR_KEY) + File.separator + propFileName);
	    InputStream inStream = new FileInputStream(initialFile);

	    propFile.load(inStream);

	} catch (IOException e) {
	    try {
		log.info("Loading csi.properties from classpath:", e);
		propFile.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName));
	    } catch (IOException e1) {
		log.debug("IO Exception", e1);
	    }
	    log.error("Error reading csi.properties", e);
	}

    }

    @Deprecated
    public CoreXmlPort getWebServicePort() {
	return amp.getAMPSoapService();
    }

    public CoreXmlPort getWebServicePort(long packageId) {
	String modValue = modeValue(packageId);
	return setModValue(modValue);
    }

    public CoreXmlPort getWebServicePortForCloseout() {
	return closeoutPort;
    }

    public Mapper getMapper() {
	if (null == mapper) {
	    init();
	}
	return mapper;
    }

    public CoreXmlPort setModValue(String modValue) {
	CoreXmlPort port = amp.getAMPSoapService();
	BindingProvider bp = (BindingProvider) port;
	Map<String, List<String>> requestHeaders = new HashMap<>();
	requestHeaders.put("ATLAS_MOD", Arrays.asList(modValue));
	bp.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);
	System.out.println("Setting modValue ATLAS_MOD to:" + modValue);
	return port;
    }

    public void setModValue1(String modValue) {

	Dispatch<SOAPMessage> disp = ((Service) amp).createDispatch(new QName("urn:connectship-com:ampcore", "AMPSoapService"), SOAPMessage.class,
		Service.Mode.MESSAGE);
	Map<String, List<String>> requestHeaders = new HashMap<>();
	requestHeaders.put("ATLAS_MOD", Arrays.asList(modValue));
	System.out.println("Setting modValue ATLAS_MOD to:" + modValue);
	disp.getRequestContext().put("PROTOCOL_HEADERS", requestHeaders);
	portMapping.put(modValue, amp.getAMPSoapService());
    }

    private static String modeValue(long nbr) {
	String str = String.valueOf(nbr);
	int n = Integer.valueOf(str.substring(str.length() - 1));
	String count = CSConfig.instance.get(CS_SERVER_COUNT);
	if (null == count) {
	    int base = 6;
	    return calculateMod(n, base);
	} else {
	    int base = Integer.valueOf(count);
	    return calculateMod(n, base);
	}

    }

    private static String calculateMod(int n, int base) {
	return String.valueOf(n % base + 1);
    }

    public static void main(String[] args) {
	int i = 0;
	while (i < 10) {
	    System.out.println(i + "mode:" + modeValue(i));
	    i++;
	}
    }

}
