package com.aiops.uim.mcs.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nimsoft.selfservice.v2.model.RawProfile;
import com.nimsoft.selfservice.v2.model.Template;
import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

public class TemplateService extends ServiceAPI implements ITemplateService {

	public TemplateService(UIMInstance uim) {
		super(uim);
	}

	public Template getTemplateById(long tid) {		
		Template template = null;
		
		try	{
			String url = getUimApiBaseURL() + "/" + "deviceoperations/" + tid + "/templates";

			Map<String,String> params = new HashMap<String,String>();
	        params.put("lookup", "by_cs_id");
	        
			ClientResponse result  = super.request(url, params);
			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else	{
				GenericType<Template> genericType = new GenericType<Template>(){};
				template = result.getEntity(genericType);
				System.out.println("Retrieved template:" + template);
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}
		
		return template;
	}

	@Override
	/*
	 * To get list of templates applcable for the device with CS_ID
	 */
	public List<Template> getAllTemplatesByDevice(long csID) {		

		List<Template> templates = null;

		try	{
			String url = getUimApiBaseURL() + "/" + "deviceoperations/" + csID + "/templates";

			Map<String,String> params = new HashMap<String,String>();
	        params.put("lookup", "by_cs_id");
	        
			ClientResponse result  = super.request(url, params);
			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else	{
				GenericType<List<Template>> genericType = new GenericType<List<Template>>(){};
				templates = result.getEntity(genericType);
				System.out.println("Retrieved " + templates.size() + " templates");
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return templates;
	}
	
	/*
	 * Get the basic profile blueprint required to create profile
	 */
	@Override
	public RawProfile getProfileForTemplate(long templateID)	{
		
		RawProfile template = null;

		try	{
			String url = getMCSWSBaseURL() + "/v1/templates/" + templateID + "/blueprint";
			ClientResponse result  = super.request(url);
			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else	{
				GenericType<RawProfile> genericType = new GenericType<RawProfile>(){};
				template = result.getEntity(genericType);
				System.out.println("Template recived: " + template.toString());
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return template;
	}
	

}
