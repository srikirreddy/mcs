package com.aiops.uim.mcs.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  com.nimsoft.selfservice.v2.model.DeviceGroup;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.aiops.uim.mcs.serviceclient.IGroupService;
import com.aiops.uim.mcs.utils.UIMInstance;

public class GroupService extends ServiceAPI implements IGroupService {
	

	public GroupService(UIMInstance uim) {
		super(uim);
		// TODO Auto-generated constructor stub
	}

	@Override
	public DeviceGroup getGroupById(long deviceId) {		
		return null;
	}	

	@Override
	public List<DeviceGroup> getAllGroups() {	
		List<DeviceGroup> groups = null;

		try	{
			String url = getUimApiBaseURL() + "/" + "groups";

			Map<String,String> params = new HashMap<String,String>();
	        params.put("groupid", "all");
	        params.put("sort", "by_id");
	        params.put("showelements", "false");
	        
			ClientResponse result  = super.request(url, params);
			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else	{
				GenericType<List<DeviceGroup>> genericType = new GenericType<List<DeviceGroup>>(){};
				groups = result.getEntity(genericType);
				System.out.println("Retrieved " + groups.size() + " groups");
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return groups;
	}

}
