package com.aiops.uim.mcs.services;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.aiops.uim.mcs.utils.RawProfileSerializer;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nimsoft.selfservice.v2.model.RawProfile;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

public class ServiceAPI {

	private final String UIMAPI_PATH = "uimapi";
	private final String MCSAPI_PATH = "mcs-ui-app/api";
	private final String MCSWS_PATH = "mcsws";

	private UIMInstance uim = null;
	private ObjectMapper profileJsonMapper = null;

	public ServiceAPI(UIMInstance uim) {
		super();
		this.uim = uim;
	}

	protected String getBaseURL() {
		return uim.getProtocol() + "://" +
				uim.getHostname() + ":" + 
				uim.getPort();
	}

	protected String getUimApiBaseURL() {
		return uim.getProtocol() + "://" +
				uim.getHostname() + ":" + 
				uim.getPort() + "/" + UIMAPI_PATH;				
	}

	protected String getMcsApiBaseURL() {
		return uim.getProtocol() + "://" +
				uim.getHostname() + ":" + 
				uim.getPort() + "/" + MCSAPI_PATH;				
	}

	protected String getMCSWSBaseURL() {
		return uim.getProtocol() + "://" +
				uim.getHostname() + ":" + 
				uim.getPort() + "/" + MCSWS_PATH;
	}

	public ClientResponse request(String url)	{

		ClientResponse result = null;

		try	{
			Client client = getUimAPIRestClient();
			WebResource webResource = client.resource(url);
			System.out.println("Requesting: "  + url);

			result  =  webResource.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return result;
	}

	public ClientResponse request(String url, Map<String,String> params)	{

		ClientResponse result = null;

		try	{
			Client client = getUimAPIRestClient();
			WebResource webResource = client.resource(url);
			MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
			params.forEach((k, v) -> queryParams.add(k,v));

			System.out.println("Requesting: "  + url);
			result  =  webResource.queryParams(queryParams).accept(MediaType.APPLICATION_XML).get(ClientResponse.class);

			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return result;
	}

	public Client getUimAPIRestClient()	{

		Client client = Client.create();
		if (uim!=null)
			uim.addBasicAuthetication(client);
		return client;
	}
	
	//Consume a post api
	public ClientResponse post(String url, Map<String, String> params, Object obj) {

		ClientResponse result = null;

		if(obj instanceof RawProfile )	{
			result = postProfile(url, params, (RawProfile)obj);
		}

		return result;
	}

	//To create a profile
		private ClientResponse postProfile(String url, Map<String, String> params, RawProfile profile) {

			ClientResponse result = null;
			String json = "";

			if (profileJsonMapper==null) {
				profileJsonMapper = new ObjectMapper(); 
				SimpleModule module = new SimpleModule(); 
				module.addSerializer(RawProfile.class, new RawProfileSerializer()); 
				profileJsonMapper.registerModule(module); 
			}

			try {
				json = profileJsonMapper.writeValueAsString(profile);
			} catch (JsonProcessingException e) {
				System.out.println("Exception" + e);
				e.printStackTrace();
			}

			try {
				Client client = getUimAPIRestClient();
				WebResource webResource = client.resource(url);
				MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
				params.forEach((k, v) -> queryParams.add(k, v));

				WebResource.Builder builder = webResource.queryParams(queryParams)
						.type(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON);

				System.out.println("Requesting: " + url);
				result = builder.post(ClientResponse.class, json);
				if (result.getStatus() != 200) {
					System.out.println("Error Response:" + result);
				}
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			}

			return result;
		}

}
