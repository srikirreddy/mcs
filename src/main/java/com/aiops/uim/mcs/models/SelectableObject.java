package com.aiops.uim.mcs.models;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimsoft.selfservice.v2.database.SelectableObjectDao;
import com.nimsoft.selfservice.v2.marshaling.AdapterCDATA;

/**
 * Class to hold one entry of the output of a callback to a probe that can be selected from a list
 * For example a disk or a process
 *
 * It could also just be a String.
 *
 * @author dobre01
 *
 */
@XmlRootElement(name = "value")
@XmlAccessorType(XmlAccessType.NONE)
// we are not marshaling all attributes. That means we need to specifically define the xmlelement for all fields that we want to
// marshal/unmarshal
public class SelectableObject implements Comparable<SelectableObject> {

	@JsonIgnore
	private int id;

	@XmlElement(name = "displayvalue")
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	
	@JsonIgnore
	private String shortName;
	
	@JsonIgnore
	private String longName;
	
	@JsonIgnore
	private String identifier; // This one is used to identify the entry, so we have the option not to display it if a profile
	
	// already exists for it.
	@XmlElement(name = "value")
	@XmlJavaTypeAdapter(AdapterCDATA.class)
	private String value; // The value in case this is a combobox - then only the shortName and value are used.
	
	private HashMap<String, String> attributes;

	public SelectableObject(String shortName, String longName, HashMap<String, String> attributes) {
		super();
		this.shortName = shortName;
		this.longName = longName;
		this.attributes = attributes;
	}

	public SelectableObject(String shortName, String longName, String value, HashMap<String, String> attributes) {
		super();
		this.shortName = shortName;
		this.longName = longName;
		this.value = value;
		this.attributes = attributes;
	}

	public SelectableObject(String shortName) {
		this.shortName = shortName;
	}

	public SelectableObject() {
	}

	public boolean contains(String filter) {
		if (shortName != null && shortName.toUpperCase().contains(filter.toUpperCase())) {
			return true;
		}
		if (longName != null && longName.toUpperCase().contains(filter.toUpperCase())) {
			return true;
		}
		if (value != null && value.toUpperCase().contains(filter.toUpperCase())) {
			return true;
		}
		if (attributes != null) {
			for (final String attr : attributes.values()) {
				if (attr != null && attr.toUpperCase().contains(filter.toUpperCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public void set(String key, String value) {
		if (attributes == null) {
			attributes = new HashMap<String, String>();
		}
		attributes.put(key, value);
	}

	public String get(String key) {
		if (attributes == null) {
			return null;
		}
		return attributes.get(key);
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getLongName() {
		return longName;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentifier() {
		if (identifier == null) {
			identifier = value;
		}
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getValue() {
		if (value == null) {
			value = identifier;
		}
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Map<String, Object> getAsMap() {
		final Map<String, Object> map = new HashMap<String, Object>(20);
		map.put("displayValue", shortName);
		map.put("value", value);
		return map;
	}

	public void save(int field) {
		/*
		 * final SelectableObjectDao sdao = SelectableObjectDao.getInstance();
		 * sdao.save(this, field);
		 */
	}

	@Override
	public String toString() {
		return "SelectableObject[\"id\":\"" + id + "\"," + (shortName != null ? "\"shortName\":\"" + escape(shortName) + "\"," : "") + (longName != null ? "\"longName\":\"" + escape(longName) + "\","
				: "") + (identifier != null ? "\"identifier\":\"" + escape(identifier) + "\"," : "") + (value != null ? "\"value\":\"" + escape(value) + "\","
						: "") + (attributes != null ? "\"attributes\":" + serializeAttributes() : "") + "]";
	}

	private String serializeAttributes() {
		final StringBuffer ser = new StringBuffer();
		ser.append("{");
		boolean first = true;
		for (final String key : attributes.keySet()) {
			if (!first) {
				ser.append(",");
			}
			ser.append("\"" + escape(key) + "\"");
			ser.append(":");
			ser.append("\"" + escape(attributes.get(key)) + "\"");
			first = false;
		}
		ser.append("}");
		return ser.toString();
	}

	private String escape(String txt) {
		final String ret = txt.replaceAll("\"", "\\\\\\\"");

		return ret;
	}

	private static Pattern pattern = Pattern.compile("\"([a-zA-Z0-9_]+\":\".*?)\"");

	public static SelectableObject parse(String serialized) {
		if (!serialized.startsWith("SelectableObject[")) {
			return null;
		}
		final SelectableObject so = new SelectableObject();
		Matcher m = pattern.matcher(serialized);
		while (m.find()) {
			final String attributeExpression = m.group(1);
			final String[] parts = attributeExpression.split("\":\"");
			if (parts.length == 2) {
				final String attribute = parts[0];
				final String value = parts[1];
				if (attribute.equalsIgnoreCase("id")) {
					so.setId(Integer.parseInt(value));
				} else if (attribute.equalsIgnoreCase("shortName")) {
					so.setShortName(value);
				} else if (attribute.equalsIgnoreCase("longName")) {
					so.setLongName(value);
				} else if (attribute.equalsIgnoreCase("identifier")) {
					so.setIdentifier(value);
				} else if (attribute.equalsIgnoreCase("value")) {
					so.setValue(value);
				}
			}
		}

		if (serialized.contains("\"attributes\":{")) {
			final HashMap<String, String> attrs = new HashMap<String, String>();

			String attributesArray = serialized.substring(serialized.indexOf("\"attributes\":{"));
			attributesArray = attributesArray.replace("\"attributes\":", "");
			attributesArray = attributesArray.substring(1, attributesArray.length() - 2);
			m = pattern.matcher(attributesArray);
			while (m.find()) {
				final String attributeExpression = m.group(1);
				final String[] parts = attributeExpression.split("\":\"");
				if (parts.length == 2) {
					attrs.put(parts[0], parts[1]);
				}
			}
			so.setAttributes(attrs);
		}

		return so;
	}

	@Override
	public int compareTo(SelectableObject o) {
		if (getShortName() == null) {
			return -1;
		}
		if (o == null || o.getShortName() == null) {
			return 1;
		}

		return getShortName().toLowerCase().compareTo(o.getShortName().toLowerCase());
	}

	@Override public int hashCode() {
		return new HashCodeBuilder(17, 31).
				append(identifier).
				append(longName).
				append(shortName).
				toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !this.getClass().isInstance(other)) {
			return false;
		}
		final SelectableObject o = (SelectableObject) other;
		if (identifier == null && o.identifier != null) {
			return false;
		}
		if (identifier != null && identifier.equals(o.identifier)) {
			return true;
		}

		if (longName == null && o.longName != null) {
			return false;
		}

		if (longName != null && longName.equals(o.longName)) {
			return true;
		}

		if (shortName == null && o.shortName != null) {
			return false;
		}

		if (shortName != null && shortName.equals(o.shortName)) {
			return true;
		}

		return false;
	}
}
