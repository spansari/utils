
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.macys.d2c.common.data.pojo.Shipment;
import com.macys.mst.wms.atlas.common.utils.ConfigUtil;
import com.macys.mst.wms.atlas.common.utils.JsonUtil;

@Slf4j
public class ShipmentServiceClient {

	
	private static final String ROOT_URL = ConfigUtil.config.get("rest.shipment.url");
	
	public static Shipment getShipmentById(String wareHouseId, String shipmentId) {
		log.debug("SHIPMENT_ROOT_URL:"+ROOT_URL);
		
		Client client = ResteasyClientBuilder.newClient();
		WebTarget target = client.target(ROOT_URL+wareHouseId+"/"+shipmentId);
		Response response = target.request().get();
		String str = response.readEntity(String.class);
		response.close();
		Shipment s = JsonUtil.fromJson(str, Shipment.class);
		return s;
	}
	
	public static Shipment getShipmentDetailsByNumber(String wareHouseId, String shipmentNumber) {
		log.debug("SHIPMENT_ROOT_URL:"+ROOT_URL);
		
		Client client = ResteasyClientBuilder.newClient();
		WebTarget target = client.target(ROOT_URL+wareHouseId+"?shipmentNumber="+shipmentNumber);
		Response response = target.request().get();
		String str = response.readEntity(String.class);
		response.close();
		Shipment s = JsonUtil.fromJson(str, Shipment.class);
		return s;
	}
	
	public static void main(String[] args) {
		System.out.println(getShipmentById("1", "11883553"));
		//System.out.println(getShipmentDetailsByNumber("1", "F028721911"));
	}
}
