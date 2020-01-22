package com.aiops.uim.mcs.serviceclient;

import java.util.List;
import com.aiops.uim.mcs.models.RawProfile;

public interface IProfileService {

	public List<RawProfile> getAllProfilesForDevice(long id);
	boolean saveProfile(RawProfile profile, Integer csId) throws Exception;
	RawProfile getProfileById(long id);
}
