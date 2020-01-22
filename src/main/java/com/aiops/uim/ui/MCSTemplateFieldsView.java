package com.aiops.uim.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aiops.uim.mcs.models.Field;
import com.aiops.uim.mcs.models.RawProfile;
import com.aiops.uim.mcs.models.SelectableObject;
import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.services.ProfileService;
//import com.nimsoft.selfservice.v2.model.Field;
//import com.nimsoft.selfservice.v2.model.Profile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class MCSTemplateFieldsView extends FormLayout{

	private ITemplateService templateService = null;
	private IProfileService profileService = null;

	private int templateId = -1;

	public MCSTemplateFieldsView(ITemplateService service, int templateId, int cs_id) throws ValidationException {

		this.templateService = service;
		this.templateId = templateId;
		FormLayout templateDetailsForm = new FormLayout();

		SplitLayout templateFieldsLayout = new SplitLayout();
		templateFieldsLayout.setOrientation(Orientation.VERTICAL);   	

		//Get fields based on template 
		RawProfile rawProfile =  templateService.getProfileForTemplate(templateId);		
		Binder<RawProfile> profileBinder = new Binder<>(RawProfile.class);
		Binder<Field> fieldBinder; 

		if(rawProfile != null) {
			List<com.aiops.uim.mcs.models.Field> fields = rawProfile.getFields();
			for(com.aiops.uim.mcs.models.Field field : fields) {

				fieldBinder = new Binder<>(Field.class);
				boolean required = false;
				if(field.isRequired())
					required = true;

				if(field.getType() != null) {
					String fieldType = field.getType();

					switch(fieldType) {
					case "text" : 
						TextField textField = new TextField();
						textField.setRequired(required);
						textField.setRequiredIndicatorVisible(required);
						textField.setLabel(field.getLabel());
						//						  if(field.getDefaultValue() != null)
						//							  textField.setValue(field.getDefaultValue());
						textField.setId(field.getId().toString());							  
						templateDetailsForm.add(textField);
						String fieldName = field.getName();
						fieldBinder.forField(textField).bind(Field::getValueAsString, Field:: setValue);
						break;

					case "textarea" :
						TextArea textArea = new TextArea();
						textArea.setLabel(field.getLabel());
						textArea.setRequired(required);
						textArea.setValue(field.getDefaultValue());
						textArea.setId(field.getId().toString());
						fieldBinder.forField(textArea).bind(Field::getValueAsString, Field:: setValue);
						templateDetailsForm.add(textArea);
						break;

					case "password" :							  
						PasswordField pwd = new PasswordField();						  
						//tf1.setRequiredIndicatorVisible(true);
						pwd.setLabel(field.getLabel());						 
						pwd.setId(field.getId().toString());	
						pwd.setPlaceholder("Enter password");
						pwd.setRequired(required);
						fieldBinder.forField(pwd).bind(Field::getValueAsString, Field:: setValue);
						templateDetailsForm.add(pwd);
						break;

					case "checkbox" :					  

						Checkbox cb = new Checkbox();	
						cb.setLabel(field.getLabel());
						
						fieldBinder.forField(cb).bind(Field::getValueAsBoolean, Field:: setValue);
						templateDetailsForm.add(cb);
						break;

					case "combobox" :					  

						ArrayList<SelectableObject> comboItems =  field.getValues();					  
						List<String> items = new ArrayList<>();
						if(comboItems != null) {
							for(SelectableObject comboItem : comboItems) {						  
								items.add(comboItem.getShortName());
							}
						}						  

						ComboBox<String> cob = new ComboBox<>("", items);
						cob.setLabel(field.getLabel());
						templateDetailsForm.add(cob);
						fieldBinder.forField(cob).bind(Field::getValueAsString, Field:: setValue);
						break;

					case "datepicker" :
						DatePicker datePicker = new DatePicker();
						templateDetailsForm.add(datePicker);    					
						break;

					case "timezone" :

						break;

					case "descriptivetext" :

						break;

					case "hyperlink" :
						Anchor anchor = new Anchor();
						break;

					case "spacer" :

						break;

					case "dynamic" :

						break;

					case "profilename" :

						break;

					case "objectselect" :

						break;

					case "qos" :

						break;

					default :

					}


				}
				fieldBinder.setBean(field);
				fieldBinder.writeBean(field);
			}

		}  

		profileBinder.setBean(rawProfile);

		templateFieldsLayout.addToPrimary(templateDetailsForm);
		Button btnCreate = new Button("Create");
		Button btnCancel = new Button("Cancel");
		//Button btnCreateProfile = new Button("Create Profile");

		btnCreate.addClickListener(e ->{
			try {				
				profileBinder.writeBean(rawProfile);
			} catch (ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (profileService==null)
				profileService = new ProfileService(MainView.getUimInstance());

			//Profile profile = createProfileobject(rawProfile);
			boolean saveStatus = false;
			Notification notification = null;
			
			try {
				saveStatus = profileService.saveProfile(rawProfile, cs_id);
			} catch (Exception e1) {
				e1.printStackTrace();
				notification = Notification.show("Error saving profile:" + e1.getMessage());
			}   

			if(saveStatus) {
				notification = Notification.show("Successfully saved profile");
			}
			
			if (notification!=null)
				notification.open();

		});

		btnCancel.addClickListener(e ->{

		});

		templateFieldsLayout.addToSecondary(btnCreate, btnCancel/* , btnCreateProfile */);
		templateFieldsLayout.setSplitterPosition(90);
		templateFieldsLayout.setSecondaryStyle("text-align", "center");

		add(templateFieldsLayout);

	}

	//Create profile object
	private RawProfile createProfileobject(RawProfile rawProfile) {
		RawProfile profile = new RawProfile();
		try {
			//profile.setRemote(remote);
			profile.setCs_id(rawProfile.getCs_id());
			profile.setGroupId(rawProfile.getGroupId());
			if ( rawProfile.getProfileId() != null ) {
				profile.setProfileId(rawProfile.getProfileId());
			}
			profile.setTemplateId(rawProfile.getTemplateId());
			profile.setAccountId(rawProfile.getAccountId());
			//profile.setContext(context);
			profile.setParentProfile(rawProfile.getParentProfile());

			List<Field> fieldList = rawProfile.getFields();
			//			 Map<Integer, String> result1 = fieldList.stream().collect(
			//		                Collectors.toMap(Field::getId, Field::getName));
			addFields(profile, fieldList);
		} catch (Exception e) {			
			System.out.println("Exception: " + e.getMessage());
		}

		return profile;
	}

	//Add fields to profile 
	private void addFields(RawProfile profile, List<Field> fieldList) throws Exception {
		/*
		 * for(Field f : fieldList ) { Map<String, Object> map = f.getAsMap(); Field
		 * field = new Field(); // Flex gives of Double NaN instead of Integer if
		 * numbers could not be parsed. Object id = f.getId(); if ( id instanceof
		 * Integer ) { field.setId((Integer)id); } field.setCfgkey(f.getCfgkey());
		 * field.setTemplate(f.getTemplate()); field.setVariable(f.getVariable());
		 * field.setValue(getFieldValue(f.getValue())); profile.addFields(field); }
		 */	
	}

	//Get field values
	private  Object getFieldValue(Object obj) {
		if ( !(obj instanceof Map) ) {
			return obj;
		}
		Map<String,Object> map = (Map<String,Object>)obj;
		SelectableObject so = new SelectableObject();
		so.setId(((Integer)map.get("id")).intValue());
		so.setIdentifier((String)map.get("identifier"));
		so.setShortName((String)map.get("shortName"));
		so.setLongName((String)map.get("longName"));
		so.setAttributes((HashMap<String,String>)map.get("attributes"));
		return so;
	}
}
