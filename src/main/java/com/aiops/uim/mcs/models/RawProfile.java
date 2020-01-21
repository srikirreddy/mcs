package com.aiops.uim.mcs.models;

import com.ca.uim.ce.selfservice.conditionaldeployment.Condition;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimsoft.selfservice.v2.marshaling.AdapterCDATA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlRootElement(name = "profile")
@XmlAccessorType(XmlAccessType.NONE)
public class RawProfile {

	@JsonIgnore
	@XmlElement(name = "profileid")
    private Integer profileId;
	
    @XmlElement(name = "templatename")
    @JsonProperty("templatename")
    private String templateName;
    
    @XmlElement(name = "templateid")
    @JsonProperty("templateid")
    private Integer templateId;
    
    @XmlElement(name = "name")
    @XmlJavaTypeAdapter(AdapterCDATA.class)
    @JsonProperty("name")
    private String profileName;

    @XmlElement(name = "cs_id")
    @JsonProperty("cs_id")
    private Integer cs_id;
    
    @XmlElement(name = "groupId")
    @JsonProperty("groupId")
    private Integer group_id;
    
    @XmlElement(name = "poller")
    @JsonProperty("poller")
    private Integer poller;
    
    @JsonProperty("field")
    @XmlElement(name = "field")
    private List<Field> fields;
    
    @JsonProperty("accountid")
    @XmlElement(name = "accountid")
    private Integer account_id;
    
    @JsonIgnore
    @XmlElement(name = "parentprofileid")
    @JsonProperty("parentProfile")
    private Integer parentProfile;
    
    @JsonProperty("status")
    @XmlElement(name = "status")
    private String status;
    
    @JsonProperty("ancestorprofileId")
    @XmlElement(name = "ancestorprofileId")
    private Integer ancestorProfile;
    
    @XmlElementWrapper(name = "subprofiles")
    @XmlElement(name = "profile")
    @JsonProperty("profile")
    private ArrayList<RawProfile> subprofiles = new ArrayList<RawProfile>();
    
    @JsonIgnore
    @XmlTransient
    private Integer retries;
    
    @JsonProperty("priority")
    @XmlElement(name = "priority")
    private Integer priority;
    
    @JsonIgnore
    @XmlElement(name = "condition")
    private Condition condition;
    
    @JsonProperty("foreachField")
    @XmlElement(name = "foreachField")
    protected Integer foreachField = null;
    
    @JsonProperty("foreachVariable")
    @XmlElement(name = "foreachVariable")
    protected String foreachVariable = null;
    
    @JsonProperty("conditionalprofilename")
    @XmlElement(name = "conditionalprofilename")
    protected String conditionalprofilename;
    
    @JsonProperty("type")
    @XmlElement(name = "type")
    protected String type = "device";
    
    @JsonProperty("profileNameVariable")
    @XmlElement(name = "profileNameVariable")
    protected String profileNameVariable = "profilename";
	
	public RawProfile() {
		super();
	}

	public void setName(String name) {
		//this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getProfileNameVariable() {
		return profileNameVariable;
	}

	public void setProfileNameVariable(String profileNameVariable) {
		this.profileNameVariable = profileNameVariable;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getProfileId() {
		return profileId;
	}

	public void setProfileId(Integer profileId) {
		this.profileId = profileId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public Integer getCs_id() {
		return cs_id;
	}

	public void setCs_id(Integer cs_id) {
		this.cs_id = cs_id;
	}

	public Integer getGroupId() {
		return group_id;
	}

	public void setGroupId(Integer group_id) {
		this.group_id = group_id;
	}

	public Integer getPoller() {
		return poller;
	}

	public void setPoller(Integer poller) {
		this.poller = poller;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Integer getAccountId() {
		return account_id;
	}

	public void setAccountId(Integer account_id) {
		this.account_id = account_id;
	}

	public Integer getParentProfile() {
		return parentProfile;
	}

	public void setParentProfile(Integer parentProfile) {
		this.parentProfile = parentProfile;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAncestorProfile() {
		return ancestorProfile;
	}

	public void setAncestorProfile(Integer ancestorProfile) {
		this.ancestorProfile = ancestorProfile;
	}

	public ArrayList<RawProfile> getSubprofiles() {
		return subprofiles;
	}

	public void setSubprofiles(ArrayList<RawProfile> subprofiles) {
		this.subprofiles = subprofiles;
	}

	public Integer getRetries() {
		return retries;
	}

	public void setRetries(Integer retries) {
		this.retries = retries;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Integer getForeachField() {
		return foreachField;
	}

	public void setForeachField(Integer foreachField) {
		this.foreachField = foreachField;
	}

	public String getForeachVariable() {
		return foreachVariable;
	}

	public void setForeachVariable(String foreachVariable) {
		this.foreachVariable = foreachVariable;
	}

	public String getConditionalprofilename() {
		return conditionalprofilename;
	}

	public void setConditionalprofilename(String conditionalprofilename) {
		this.conditionalprofilename = conditionalprofilename;
	}
	
	/**
     * @return the subProfiles
     */
    public ArrayList<RawProfile> getSubProfiles() {
        if (subprofiles == null) {
            return new ArrayList<RawProfile>();
        }
        return subprofiles;
    }
    
    /**
     * @return the fields
     */
    public List<Field> getFields() {
        return fields;
    }

//	@JsonIgnore
//	public Map<String, Object> getAsMap() {
//        final Map<String, Object> map = new HashMap<String, Object>(20);
//        map.put("profileName", profileName);
//        map.put("template", getTemplateId());
//        if (cs_id != null && cs_id > 0) {
//            map.put("cs_id", cs_id);
//        }
//        if (group_id != null && group_id > 0) {
//            map.put("group_id", group_id);
//        }
//        if (priority != null) {
//            map.put("priority", priority);
//        }
//        map.put("ancestorprofile", ancestorProfile);
//        map.put("status", status);
//        map.put("retries", retries);
//        map.put("poller", poller);
//        if (parentProfile != null && parentProfile > 0) {
//            map.put("parentProfile", parentProfile);
//        }
//        if (account_id != null && account_id > 0) {
//            map.put("account_id", account_id);
//        }
//        map.put("status", status);
//        return map;
//
//    }    
}
