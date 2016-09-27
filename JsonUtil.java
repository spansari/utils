package com.macys.mst.wms.atlas.common.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import com.mst.csi.data.CartonRequestVo;
import com.mst.csi.data.DimensionsVo;
import com.mst.csi.data.NameAddressVo;
import com.mst.csi.data.RateRequestVo;
import com.mst.csi.data.WeightVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {

    public static <T extends Object> T fromJson(String str, Class<T> type) {
	T t = null;
	ObjectMapper mapper = new ObjectMapper();
	mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	try {
	    t = mapper.readValue(str, type);
	} catch (JsonParseException e) {
		log.error("JsonParseException in ObjectMapper.readValue(): {}",e);
	} catch (JsonMappingException e) {
		log.error("JsonMappingException in ObjectMapper.readValue(): {}",e);
	} catch (IOException e) {
		log.error("IOException in ObjectMapper.readValue(): {}",e);
	}

	return t;
    }

    public static String toJson(Object obj) {
	String str = null;

	ObjectMapper mapper = new ObjectMapper();

	try {
	    str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
	} catch (JsonParseException e) {
		log.error("JsonParseException in ObjectMapper.writerWithDefaultPrettyPrinter(): {}",e);
	} catch (JsonMappingException e) {
		log.error("JsonMappingException in ObjectMapper.writerWithDefaultPrettyPrinter(): {}",e);
	} catch (IOException e) {
		log.error("IOException in ObjectMapper.writerWithDefaultPrettyPrinter(): {}",e);
	}

	return str;
    }

    public static String toJsonForPheme(Object obj) {

	Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().setLongSerializationPolicy(LongSerializationPolicy.STRING).create();

	return gson.toJson(obj);

    }

    public static void main(String[] args) {

//	Carrier c = new Carrier();
//
//	c.setCarrierCode("1");
//	c.setCarrierId(123l);
//
//	System.out.println(toJsonForPheme(c));
	
	System.out.println("Hi");
	
	RateRequestVo req = new RateRequestVo();
	
	req.setShipper("MB");
	
	NameAddressVo address = new NameAddressVo();
	address.setAddress1("2700 Addison Ln");
	address.setCity("Alpharetta");
	address.setStateProvince("GA");
	address.setPostalCode("30005");
	req.setConsignee(address);
	
	req.setShipDate(new Date());
	
	req.setPackageId(1l);
	
	CartonRequestVo carton = new CartonRequestVo();
	
	
	DimensionsVo dimensions = new DimensionsVo();
	dimensions.setHeight(1.0);
	dimensions.setLength(1.0);
	dimensions.setWidth(1.0);
	WeightVo weight = new WeightVo();
	weight.setValue("3.3 lbs");
	carton.setDimensions(dimensions);
	carton.setWeight(weight);
	
	List<CartonRequestVo> list =  new ArrayList();
	
	list.add(carton);
	
	req.setCartonList(list);
	
	List<String> serviceList = new ArrayList();
	
	serviceList.add("CONNECTSHIP_UPS.UPS.GND");
	
	req.setServiceList(serviceList);
	
	System.out.println(toJson(req));
	
 
    }

}
