package com.aiops.uim.mcs.serviceclient;

import java.util.List;

import com.aiops.uim.mcs.models.RawProfile;
import com.nimsoft.selfservice.v2.model.Template;

public interface ITemplateService {

	Template getTemplateById(long templateId);
	List<Template> getAllTemplatesByDevice(long deviceId);
	public RawProfile getProfileForTemplate(long templateID);
}