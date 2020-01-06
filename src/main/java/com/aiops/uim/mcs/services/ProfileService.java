package com.aiops.uim.mcs.services;

import com.nimsoft.selfservice.v2.model.RawProfile;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProfileService extends ServiceAPI implements IProfileService {

	public ProfileService(UIMInstance uim) {
		super(uim);
		// TODO Auto-generated constructor stub
	}

	public RawProfile getProfileById(long profile_id) {

		RawProfile profile = null;

		try	{
			String url = getBaseURL() + "/profiles/" + profile_id;
			ClientResponse result  =  super.request(url);

			if(result.getStatus() != 200)	{
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else	{
				GenericType<RawProfile> genericType = new GenericType<RawProfile>(){};
				profile = result.getEntity(genericType);
				System.out.println("Profile recived: " + profile.toString());
			}
		}
		catch(Exception e)	{
			System.out.println("Exception: " + e);
		}

		return profile;
	}

	@Override
	public List<RawProfile> getAllProfilesForDevice(long id) {

		List<RawProfile> profiles = null;

		try {
			String url = getUimApiBaseURL() + "/deviceoperations/" + id + "/profiles";
			ClientResponse result  =  super.request(url);

			if(result.getStatus() != 200) {
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else {
				GenericType<List<RawProfile>> genericType = new GenericType<List<RawProfile>>(){};
				profiles = result.getEntity(genericType);
				System.out.println("Profiles recived: " + profiles.size());
			}
		}
		catch(Exception e) {
			System.out.println("Exception: " + e);
		}

		return profiles;
	}

	@Override
	public boolean saveProfile(RawProfile profile, long lDevId) {
		// TODO Auto-generated method stub

		ObjectMapper mapper = new ObjectMapper();
		String json = null;

		try {
			json = mapper.writeValueAsString(profile);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		String url = getMCSWSBaseURL() + "/v1/devices/"+ lDevId + "/profiles";
		Map<String, String> params = new HashMap<String,String>();
		params.put("identifier", Long.toString(lDevId));
		params.put("body", json);
		params.put("lookup", "by_cs_id");
		params.put("attemptDeployment", "true");

		try {
			ClientResponse result  =  super.post(url, params);

			if(result.getStatus() != 200) {
				System.out.println("Error requesting: "  + url + " Status:" + result.getStatus());
			}
			else {
				GenericType<RawProfile> genericType = new GenericType<RawProfile>(){};
				profile = result.getEntity(genericType);
				System.out.println("Profile recived: " + profile.toString());
			}
		}
		catch(Exception e) {
			System.out.println("Exception: " + e);
		}


		return false;
	}

}
