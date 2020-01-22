package com.aiops.uim.mcs.services;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiops.uim.mcs.models.RawProfile;
import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.aiops.uim.mcs.utils.exceptions.ProfileCreationException;

/**
 * @author Srikanth Kondaveti
 *
 */
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

	/**
	 *  To save a profile	
	 */
	@Override
	public boolean saveProfile(RawProfile profile, Integer csId) throws Exception {

		boolean ret = false;

		//TODO: GUI should set this and return
		profile.setCs_id(csId);	
		profile.setPoller(1);

		//String url = getMCSWSBaseURL() + "/v1/devices/"+ csId + "/profiles";
		String url = super.getUimApiBaseURL() + "/deviceoperations/"+ csId + "/profiles";
		Map<String, String> params = new HashMap<String,String>();
		params.put("identifier", Long.toString(csId));
		params.put("lookup", "by_cs_id");
		params.put("attemptDeployment", "true");

		try {
			ClientResponse result  =  super.post(url, params, profile);

			if(result.getStatus() == 200) {
				ret = true;
			}
			else	{
				GenericType<String> response = new GenericType<String>(){};
				System.out.println("Profile creation failed: " +response);
				throw new ProfileCreationException(result.getEntity(String.class));
			}
		}
		catch (ProfileCreationException pe) {
			throw pe;
		}

		return ret;
	}


}
