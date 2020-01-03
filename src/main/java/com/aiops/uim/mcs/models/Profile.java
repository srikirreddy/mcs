package com.aiops.uim.mcs.models;

import java.util.List;

public class Profile {
	int profileId;
	String templateName;
	int templateId;
	int cs_id;
	int groupId;
	String poller;
	List<Field> fields;
	int accountId;
	int parentProfileId;
	String status;
	int ancestorprofileId;
	String profile;
	String priority;
	String foreachField;
	String foreachVariable;
	String foreachValueRegex;
	String conditionalprofilename;
	String profileNameVariable;
	String type;
	String icon;
	
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	
	public int getCs_id() {
		return cs_id;
	}
	public void setCs_id(int cs_id) {
		this.cs_id = cs_id;
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public String getPoller() {
		return poller;
	}
	public void setPoller(String poller) {
		this.poller = poller;
	}
	
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	
	public int getParentProfileId() {
		return parentProfileId;
	}
	public void setParentProfileId(int parentProfileId) {
		this.parentProfileId = parentProfileId;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getAncestorprofileId() {
		return ancestorprofileId;
	}
	public void setAncestorprofileId(int ancestorprofileId) {
		this.ancestorprofileId = ancestorprofileId;
	}
	
	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	
	public String getForeachField() {
		return foreachField;
	}
	public void setForeachField(String foreachField) {
		this.foreachField = foreachField;
	}
	
	public String getForeachVariable() {
		return foreachVariable;
	}
	public void setForeachVariable(String foreachVariable) {
		this.foreachVariable = foreachVariable;
	}
	
	public String getForeachValueRegex() {
		return foreachValueRegex;
	}
	public void setForeachValueRegex(String foreachValueRegex) {
		this.foreachValueRegex = foreachValueRegex;
	}
	
	public String getConditionalprofilename() {
		return conditionalprofilename;
	}
	public void setConditionalprofilename(String conditionalprofilename) {
		this.conditionalprofilename = conditionalprofilename;
	}
	
	public String getProfileNameVariable() {
		return profileNameVariable;
	}
	public void setProfileNameVariable(String profileNameVariable) {
		this.profileNameVariable = profileNameVariable;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	

}
