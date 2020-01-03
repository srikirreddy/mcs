package com.aiops.uim.mcs.serviceclient;

import java.util.List;

import com.nimsoft.selfservice.v2.model.DeviceGroup;

public interface IGroupService {

	DeviceGroup getGroupById(long deviceId);
	List<DeviceGroup> getAllGroups();
}