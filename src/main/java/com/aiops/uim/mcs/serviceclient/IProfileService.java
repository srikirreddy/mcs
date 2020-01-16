package com.aiops.uim.mcs.serviceclient;

import java.util.List;

import com.nimsoft.selfservice.v2.model.Profile;
import com.nimsoft.selfservice.v2.model.RawProfile;

public interface IProfileService {

	public List<RawProfile> getAllProfilesForDevice(long id);
	boolean saveProfile(Profile profile, Integer csId);
	RawProfile getProfileById(long id);
}
