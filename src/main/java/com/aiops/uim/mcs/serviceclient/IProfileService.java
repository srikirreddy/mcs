package com.aiops.uim.mcs.serviceclient;

import com.nimsoft.selfservice.v2.model.RawProfile;

public interface IProfileService {

	boolean saveProfile(RawProfile profile);

	RawProfile getProfileById(long id);
}
