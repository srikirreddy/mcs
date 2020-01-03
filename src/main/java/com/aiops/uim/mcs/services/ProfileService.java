package com.aiops.uim.mcs.services;

import com.nimsoft.selfservice.v2.model.RawProfile;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.utils.UIMInstance;

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
	public boolean saveProfile(RawProfile profile) {
		// TODO Auto-generated method stub
		return false;
	}

}
