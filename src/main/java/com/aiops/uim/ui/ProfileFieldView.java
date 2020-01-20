package com.aiops.uim.ui;

import java.util.ArrayList;
import java.util.List;

import com.aiops.uim.mcs.models.Field;
import com.aiops.uim.mcs.models.RawProfile;
import com.aiops.uim.mcs.models.SelectableObject;
import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.services.TemplateService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

@Route("createsubprofile")
public class ProfileFieldView extends FormLayout {

	private ITemplateService templateService = null;
	private IProfileService profileService = null;	
	private int templateId = 228;

	public ProfileFieldView() throws ValidationException {
		 
		this.templateService = new TemplateService(MainView.getUimInstance());;
		this.templateId = templateId;
		FormLayout templateDetailsForm = new FormLayout();

		SplitLayout templateFieldsLayout = new SplitLayout();
		templateFieldsLayout.setOrientation(Orientation.VERTICAL);   	

		//Get fields based on template 
		RawProfile rawProfile =  templateService.getProfileForTemplate(templateId);		
		Binder<RawProfile> profileBinder = new Binder<>(RawProfile.class);
		Binder<Field> fieldBinder; 

		if(rawProfile != null) {
			List<Field> fields = rawProfile.getFields();
			for(Field field : fields) {

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
						templateDetailsForm.add(textArea);
						break;

					case "password" :							  
						PasswordField pwd = new PasswordField();						  
						//tf1.setRequiredIndicatorVisible(true);
						pwd.setLabel(field.getLabel());						 
						pwd.setId(field.getId().toString());	
						pwd.setPlaceholder("Enter password");
						pwd.setRequired(required);
						templateDetailsForm.add(pwd);
						break;

					case "checkbox" :					  

						Checkbox cb = new Checkbox();	
						cb.setLabel(field.getLabel());
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

		btnCreate.addClickListener(e ->{
			try {				
				profileBinder.writeBean(rawProfile);
			} catch (ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});

		btnCancel.addClickListener(e ->{

		});

		templateFieldsLayout.addToSecondary(btnCreate,btnCancel);
		templateFieldsLayout.setSplitterPosition(90);
		templateFieldsLayout.setSecondaryStyle("text-align", "center");

		add(templateFieldsLayout);

	}
}
