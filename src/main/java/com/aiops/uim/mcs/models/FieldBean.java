package com.aiops.uim.mcs.models;

import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nimsoft.selfservice.v2.marshaling.AdapterCDATA;
import com.nimsoft.selfservice.v2.model.Context;
//import com.nimsoft.selfservice.v2.model.SelectableObject;

@XmlAccessorType(XmlAccessType.FIELD)
public class FieldBean {

  @XmlElement(name = "id")
  protected Integer id;
  @XmlElement(name = "type")
  protected String type;
  @XmlElement(name = "name")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String name;
  
  @JsonProperty("cfgkey")
  @XmlElement(name = "cfgkey")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String cfgkey;
  
  @XmlElement(name = "variable")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String variable;
  
  @XmlElement(name = "validation")
  protected String validation;
  
  @XmlElement(name = "datatype")
  protected String datatype;
  
  @XmlElement(name = "position")
  protected Integer position;
  
  @XmlElement(name = "label")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String label;
  
  @JsonProperty("labelposition")
  @XmlElement(name = "labelposition")
  protected String labelPosition;
  
  @XmlElement(name = "helptext")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String helptext;
  
  @XmlElement(name = "url")
  protected String url;
  
  @XmlElement(name = "length")
  protected Integer length;
  //protected Integer numlines;
  @XmlElement(name = "data")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String data;
  @XmlElement(name = "required")
  protected Boolean required;
  
  @XmlElement(name = "value")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected Object value;
  @XmlElement(name = "readonly")
  protected Boolean readonly;
  
  @XmlElement(name = "immutable")
  protected Boolean immutable;
  @XmlElement(name = "editable")
  protected Boolean editable;
  @XmlElement(name = "hidden")
  protected Boolean hidden;
  
  @XmlElement(name = "relatedField")
  protected Integer relatedField = null;
  @XmlElement(name = "relatedFieldValue")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String relatedFieldValue = null;
  @XmlElement(name = "values")
  protected ArrayList<SelectableObject> values;
  
  @JsonIgnore
  protected ArrayList<String> options;
  
  @JsonIgnore
  protected Integer numlines;
  
  @JsonProperty("containerpath")
  @XmlElement(name = "containerpath")
  protected String containerPath;
  
  @XmlElement(name = "callback")
  protected Integer callback;
  
  @XmlElement(name = "template")
  protected Integer template;
  
  @XmlElement(name = "acl")
  protected String acl;
  @XmlElement(name = "legacyVariable")
  protected String legacyVariable;
  
  @XmlElement(name = "omitSectionCondition")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String omitSectionCondition;
  
  @XmlElement(name = "salt")
  @XmlJavaTypeAdapter(AdapterCDATA.class)
  protected String salt;
  
  @XmlElement(name = "persist")
  protected Boolean persist;
  
  @JsonProperty("context")
  @XmlElement(name = "context")
  protected Context[] contexts;
  
  @XmlElement(name = "defaultcfgkey")
  protected String defaultcfgkey = null;
  
  @JsonProperty("defaultvalue")
  @XmlElement(name = "defaultvalue")
  protected String defaultValue = null;
  
  @XmlElement(name = "calculation")
  protected String calculation = null;

  public String getAcl() {
    return acl;
  }

  public Integer getCallback() {
    return callback;
  }

  public String getContainerPath() {
    return containerPath;
  }

  public String getData() {
    return data;
  }

  public Context[] getContext() {
    if (contexts == null) {
      return null;
    }

    return Arrays.copyOf(contexts, contexts.length);
  }

  public void setContexts(Context[] contexts) {
    if (contexts == null) {
      this.contexts = null;
    } else {
      this.contexts = Arrays.copyOf(contexts, contexts.length);
    }
  }

  public String getDatatype() {
    return datatype;
  }

  public void setDefaultValue(String defaultValue) {
    /*
     * if (this.defaultValue != null) {
     * Thread.dumpStack();
     * System.exit(1);
     * }
     */
    this.defaultValue = defaultValue;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getHelptext() {
    return helptext;
  }

  public String getLabel() {
    return label;
  }

  public String getLabelPosition() {
    return labelPosition;
  }

  public String getLegacyVariable() {
    return legacyVariable;
  }

  public int getLength() {
    if (length == null) {
      return 0;
    }
    return length;
  }

  public String getName() {
    return name;
  }

	/*
	 * public Integer getNumlines() { return numlines; }
	 */

  public String getOmitSectionCondition() {
    return omitSectionCondition;
  }

  public Integer getPosition() {
    return position;
  }

  public Boolean getReadonly() {
    return readonly;
  }

  public Integer getRelatedField() {
    return relatedField;
  }

  public String getRelatedFieldValue() {
    return relatedFieldValue;
  }

  public String getSalt() {
    return salt;
  }

  public Integer getTemplate() {
    return template;
  }

  public String getType() {
    return type;
  }

  public String getUrl() {
    return url;
  }

  public String getCalculation() {
    return calculation;
  }

  public void setCalculation(String calculation) {
    this.calculation = calculation;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public void setValidation(String validation) {
    this.validation = validation;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public void setTemplate(Integer template) {
    this.template = template;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setRelatedFieldValue(String relatedFieldValue) {
    this.relatedFieldValue = relatedFieldValue;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
  }

  @XmlTransient
  public void setReadonly(Boolean readonly) {
    this.readonly = readonly;
  }

  public void setRelatedField(Integer relatedField) {
    this.relatedField = relatedField;
  }

  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public void setHelptext(String helptext) {
    this.helptext = helptext;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  public void setPersist(Boolean persist) {
    this.persist = persist;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setImmutable(Boolean immutable) {
    this.immutable = immutable;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }

  public void setLegacyVariable(String legacyVariable) {
    this.legacyVariable = legacyVariable;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public void setName(String name) {
    this.name = name;
  }

	/*
	 * public void setNumlines(Integer numlines) { this.numlines = numlines; }
	 */

  public void setOmitSectionCondition(String omitSectionCondition) {
    this.omitSectionCondition = omitSectionCondition;
  }

  public void setContainerPath(String containerPath) {
    this.containerPath = containerPath;
  }

  public void setData(String data) {
    this.data = data;
  }

  public void setDatatype(String datatype) {
    this.datatype = datatype;
  }

  public void setCfgkey(String cfgkey) {
    this.cfgkey = cfgkey;
  }

  public void setCallback(Integer callback) {
    this.callback = callback;
  }

  public void setAcl(String acl) {
    this.acl = acl;
  }

  public boolean isRequired() {
    return required == null ? false : required;
  }

  public boolean isEditable() {
    return editable == null ? false : editable;
  }

  public boolean isHidden() {
    return hidden == null ? false : hidden;
  }

  public Boolean shouldPersist() {
    // If persist is not present in the field, calculate value based on cfgkey
    // and variable
    if (persist == null) {
      persist = cfgkey != null || variable != null;
    }

    return persist;
  }

  public boolean isImmutable() {
    return immutable == null ? false : immutable;
  }

  public String getVariable() {
    return variable;
  }

  public String getValidation() {
    return validation;
  }

  public Integer getId() {
    return id;
  }

  public String getCfgkey() {
    return cfgkey;
  }
}