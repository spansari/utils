package com.macys.mst.wms.atlas.common.rest.clients;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import com.macys.d2c.common.data.pojo.Package;
import com.macys.mst.wms.atlas.common.utils.ConfigUtil;
import com.macys.mst.wms.atlas.common.utils.JsonUtil;

@Slf4j
public class PackageServiceClient {

	private static final String ROOT_URL = ConfigUtil.config.get("rest.package.url");
	
	public static Package getPackageById(String wareHouseId, String packageId) {
		log.debug("PACKAGE_ROOT_URL:"+ROOT_URL);
		
		Client client = ResteasyClientBuilder.newClient();
		WebTarget target = client.target(ROOT_URL+wareHouseId+"/"+packageId);
		Response response = target.request().get();
		String str = response.readEntity(String.class);
		response.close();
		Package p = JsonUtil.fromJson(str, Package.class);

		return p;
	}
	
	public static Package getPackageDetailsByNumber(String wareHouseId, String packageNumber) {
		log.debug("PACKAGE_ROOT_URL:"+ROOT_URL);
		
		Client client = ResteasyClientBuilder.newClient();
		WebTarget target = client.target(ROOT_URL+wareHouseId+"?packageNumber="+packageNumber);
		Response response = target.request().get();
		
		String str = response.readEntity(String.class);
		response.close();
		Package p = JsonUtil.fromJson(str, Package.class);
		return p;
	}
	
		
	public static void main(String[] args) {
		System.out.println(getPackageDetailsByNumber("1", "00000000010286375969"));
		//System.out.println(getPackageById("1", "22942758"));
	}
	
}
