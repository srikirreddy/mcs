package com.aiops.uim.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import com.aiops.uim.mcs.models.RawProfile;
import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.services.ProfileService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.QueryParameters;

public class MCSProfilesGridView extends VerticalLayout {
	
	private IProfileService profileService = null;
	
    // used to generate some random data
    private final Random random = new Random();
    RawProfile rawProfile;
    Integer templateId;
    TextField filterText;

    public MCSProfilesGridView(int cs_id) {
    	 
        TreeGrid<RawProfile> treeGrid = new TreeGrid<>();
        String editText = "Edit";
        //Get list of profiles for a device
        setVisible(true);
        profileService = new ProfileService(MainView.getUimInstance());
        List<RawProfile> profiles = profileService.getAllProfilesForDevice(cs_id);
    	
        filterText = new TextField();
        filterText.setEnabled(true);
    	filterText.setPlaceholder("Filter by profile name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList(treeGrid, profiles, cs_id, filterText.getValue().toLowerCase(), false));    	
       
        //addComponentAsFirst(treeGrid);
        Binder<RawProfile> binder = new Binder<>(RawProfile.class);
        Div validationStatus = new Div();
        TextField firstNameField = new TextField();
        binder.forField(firstNameField)
                .withValidator(new StringLengthValidator("First name length must be between 3 and 50.", 3, 50))
                .withStatusLabel(validationStatus).bind("profileName");
      
        Editor<RawProfile> editor = treeGrid.getEditor();
        editor.setBinder(binder);
        editor.setBuffered(true);
        validationStatus.setId("validation");
        Collection<Button> editButtons = Collections
                .newSetFromMap(new WeakHashMap<>());


        Grid.Column<RawProfile> profileColumn = treeGrid.addHierarchyColumn(RawProfile::getProfileName).setHeader("Profile Name");
        profileColumn.setEditorComponent(firstNameField);
        // Or you can use an ordinary function to setup the component
        Grid.Column<RawProfile> editorColumn = treeGrid.addComponentColumn(item -> createRemoveButton(treeGrid, item)).setWidth("4%");
       
        treeGrid.addColumn(RawProfile::getStatus).setHeader("Status");//.setCaption("Last Modified");
        treeGrid.addColumn(RawProfile::getTemplateId).setHeader("Template Id");//.setCaption("Hours Done");
        
        // add the list of root projects and specify a provider of sub-projects
        updateList(treeGrid, profiles, cs_id, filterText.getValue(), true);        
        treeGrid.setPageSize(2);        
        //treeGrid.setItems(generateProjectsForYears(2010, 2016), Project::getSubProjects);
        add(filterText, treeGrid);
        treeGrid.addItemClickListener(rec-> {
        	templateId = rec.getItem().getTemplateId();
        });
        treeGrid.setSelectionMode(SelectionMode.MULTI);
    }
    
    //Get profiles for a device
    public void updateList(TreeGrid<RawProfile> treeGrid, List<RawProfile> profiles, int cs_id, String profileNameFilter, boolean isFirstTimeLoad) {
    	if(isFirstTimeLoad) {  
            treeGrid.setItems(profiles, RawProfile::getSubProfiles);
    	}
    	else {
    		 List<RawProfile> filteredProfiles = getProfilesByName(profiles, profileNameFilter);
             treeGrid.setItems(filteredProfiles, RawProfile::getSubProfiles);
    	}    	
    }
    
    //Filter profiles based on profile name filter
    private List<RawProfile> getProfilesByName(List<RawProfile> profiles, String profileNameFilter) {
    	
    	List<RawProfile> filteredProfiles = profiles.stream()
                .filter(x -> x.getProfileName().toLowerCase().contains(profileNameFilter))
                .collect(Collectors.toList());
        return filteredProfiles;
    }
    private Icon createRemoveButton(Grid<RawProfile> grid, RawProfile item) {
    	Icon button = new Icon(VaadinIcon.MINUS);
    	button.setSize("20px");
    	button.setColor("green");
    	if(item.getTemplateId() > 150 && item.getTemplateId() < 160) {
         button = new Icon(VaadinIcon.PLUS);
         button.setSize("20px");
     	 button.setColor("green");
//        , clickEvent -> {
//            ListDataProvider<RawProfile> dataProvider = (ListDataProvider<RawProfile>) grid
//                    .getDataProvider();
//            dataProvider.getItems().remove(item);
//            dataProvider.refreshAll();
//        });
    	}
    	button.addClickListener(e-> {
    		List<String> list = new ArrayList<String>();
			Map<String, List<String>> parametersMap = new HashMap<String, List<String>>();			
			list = new ArrayList<String>();
			list.add(String.valueOf(item.getTemplateId()));
			parametersMap.put("templateid", list);					 
			QueryParameters qp = new QueryParameters(parametersMap);
//			getUI().ifPresent(ui ->
//	           ui.navigate("createsubprofile", qp));
			 
			try {
				filterText.setEnabled(false);
				add(new ProfileFieldView(item.getTemplateId()));
			} catch (ValidationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	});
        return button;
    }
}
