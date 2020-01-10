package com.aiops.uim.mcs.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.nimsoft.selfservice.v2.model.Field;
import com.nimsoft.selfservice.v2.model.RawProfile;

public class RawProfileSerializer extends StdSerializer<RawProfile> {

	public RawProfileSerializer() {
		this(null);
	}

	public RawProfileSerializer(Class<RawProfile> p) {
		super(p);
	}

	@Override
	public void serialize(RawProfile value, 
			JsonGenerator jgen, SerializerProvider provider) 
					throws IOException, JsonProcessingException {

		jgen.writeStartObject();
		//jgen.writeNumberField("profileid", );
		addStringIfNotNull(jgen, "templatename", value.getTemplateName());
		addNumberIfNotNull(jgen, "templateid", value.getTemplateId());
		addStringIfNotNull(jgen, "name", value.getTemplateName());
		addNumberIfNotNull(jgen, "cs_id", value.getCs_id());

		jgen.writeArrayFieldStart("field");
		List<Field> fields = value.getFields();
		for(Field f: fields)	{
			jgen.writeStartObject();			
			addNumberIfNotNull(jgen, "id", f.getId());
			addStringIfNotNull(jgen, "type", f.getType());
			addStringIfNotNull(jgen, "name", f.getName());
			addStringIfNotNull(jgen, "cfgkey", f.getCfgkey());
			addStringIfNotNull(jgen, "value", f.getValueAsString());
			jgen.writeEndObject();
		}
		jgen.writeEndArray();
		jgen.writeEndObject();
	}
	
	private void addStringIfNotNull(JsonGenerator jgen, String name, String value) 
			throws JsonGenerationException, IOException {
		
		if(value!=null && value.trim().length()>0)	{
			jgen.writeStringField(name, value);
		}
	}
	
	private void addNumberIfNotNull(JsonGenerator jgen, String name, Integer value) 
			throws JsonGenerationException, IOException {
		
		if(value!=null)	{
			jgen.writeNumberField(name, value);
		}
	}
}
