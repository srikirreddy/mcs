package com.aiops.uim.mcs.serviceclient;

import java.util.List;
import com.nimsoft.selfservice.v2.model.RawProfile;

public interface IProfileService {

	public List<RawProfile> getAllProfilesForDevice(long id);
	boolean saveProfile(RawProfile profile, long devID);
	RawProfile getProfileById(long id);
}
