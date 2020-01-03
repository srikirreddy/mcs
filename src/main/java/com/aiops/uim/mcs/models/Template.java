package com.aiops.uim.mcs.models;

public class Template {
	
	String templateName;
	int templateId;
	String monitoringagenttemplatename;
	String monitoringagentresolution;
	String version;
	String deploymethod;
	String probe;
	String probeversion;
	int maxprofiles;
	String description;
	boolean production;
	boolean remote;
	String type;
	String icon;
	int parent;
	String acl;
	String globalcontexts;
	boolean isComposite;
	String validationreport;
	String profileNameVariable;
	String directives;
	String container;
	String subprofiles;
	String templateFilters;
	String legacyTemplateName;
	String dependentTemplates;
	
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
	
	public String getMonitoringagenttemplatename() {
		return monitoringagenttemplatename;
	}
	public void setMonitoringagenttemplatename(String monitoringagenttemplatename) {
		this.monitoringagenttemplatename = monitoringagenttemplatename;
	}
	
	public String getMonitoringagentresolution() {
		return monitoringagentresolution;
	}
	public void setMonitoringagentresolution(String monitoringagentresolution) {
		this.monitoringagentresolution = monitoringagentresolution;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getDeploymethod() {
		return deploymethod;
	}
	public void setDeploymethod(String deploymethod) {
		this.deploymethod = deploymethod;
	}
	
	public String getProbe() {
		return probe;
	}
	public void setProbe(String probe) {
		this.probe = probe;
	}
	
	public String getProbeversion() {
		return probeversion;
	}
	public void setProbeversion(String probeversion) {
		this.probeversion = probeversion;
	}
	
	public int getMaxprofiles() {
		return maxprofiles;
	}
	public void setMaxprofiles(int maxprofiles) {
		this.maxprofiles = maxprofiles;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public boolean isProduction() {
		return production;
	}
	public void setProduction(boolean production) {
		this.production = production;
	}
	
	public boolean isRemote() {
		return remote;
	}
	public void setRemote(boolean remote) {
		this.remote = remote;
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
	
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	
	public String getAcl() {
		return acl;
	}
	public void setAcl(String acl) {
		this.acl = acl;
	}
	
	public String getGlobalcontexts() {
		return globalcontexts;
	}
	public void setGlobalcontexts(String globalcontexts) {
		this.globalcontexts = globalcontexts;
	}
	
	public boolean isComposite() {
		return isComposite;
	}
	public void setComposite(boolean isComposite) {
		this.isComposite = isComposite;
	}
	
	public String getValidationreport() {
		return validationreport;
	}
	public void setValidationreport(String validationreport) {
		this.validationreport = validationreport;
	}
	
	public String getProfileNameVariable() {
		return profileNameVariable;
	}
	public void setProfileNameVariable(String profileNameVariable) {
		this.profileNameVariable = profileNameVariable;
	}
	
	public String getDirectives() {
		return directives;
	}
	public void setDirectives(String directives) {
		this.directives = directives;
	}
	
	public String getContainer() {
		return container;
	}
	public void setContainer(String container) {
		this.container = container;
	}
	
	public String getSubprofiles() {
		return subprofiles;
	}
	public void setSubprofiles(String subprofiles) {
		this.subprofiles = subprofiles;
	}
	
	public String getTemplateFilters() {
		return templateFilters;
	}
	public void setTemplateFilters(String templateFilters) {
		this.templateFilters = templateFilters;
	}
	
	public String getLegacyTemplateName() {
		return legacyTemplateName;
	}
	public void setLegacyTemplateName(String legacyTemplateName) {
		this.legacyTemplateName = legacyTemplateName;
	}
	
	public String getDependentTemplates() {
		return dependentTemplates;
	}
	public void setDependentTemplates(String dependentTemplates) {
		this.dependentTemplates = dependentTemplates;
	}
}
