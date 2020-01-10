package com.aiops.uim.ui;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.services.ProfileService;
import com.nimsoft.selfservice.v2.model.RawProfile;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

@Route("mcs/profiles")
public class MCSProfilesGridView extends VerticalLayout {
	
	private IProfileService profileService = null;
	
    // used to generate some random data
    private final Random random = new Random();
    RawProfile rawProfile;

    public MCSProfilesGridView(int cs_id) {
    	 
        TreeGrid<RawProfile> treeGrid = new TreeGrid<>();
    	
    	TextField filterText = new TextField();
    	filterText.setPlaceholder("Filter by profile name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList(treeGrid, cs_id, filterText.getValue().toLowerCase()));    	
       
        //addComponentAsFirst(treeGrid);
        treeGrid.addHierarchyColumn(RawProfile::getProfileName).setHeader("Profile Name");
        treeGrid.addColumn(RawProfile::getStatus).setHeader("Status");//.setCaption("Last Modified");
        treeGrid.addColumn(RawProfile::getTemplateId).setHeader("Template Id");//.setCaption("Hours Done");
        
        // add the list of root projects and specify a provider of sub-projects
        updateList(treeGrid, cs_id, filterText.getValue());
        //treeGrid.setItems(profiles, RawProfile::getSubProfiles);
        treeGrid.setPageSize(2);        
        //treeGrid.setItems(generateProjectsForYears(2010, 2016), Project::getSubProjects);
        add(filterText, treeGrid);
    }
    
    //Get profiles for a device
    public void updateList(TreeGrid treeGrid, int cs_id, String profileNameFilter) {
    	profileService = new ProfileService(MainView.getUimInstance());
        List<RawProfile> profiles = profileService.getAllProfilesForDevice(cs_id);
        List<RawProfile> filteredProfiles = getProfilesByName(profiles, profileNameFilter);
        treeGrid.setItems(filteredProfiles);
    }
    
    //Filter profiles based on profile name filter
    private List<RawProfile> getProfilesByName(List<RawProfile> profiles, String profileNameFilter) {
    	
    	List<RawProfile> filteredProfiles = profiles.stream()
                .filter(x -> x.getProfileName().toLowerCase().contains(profileNameFilter))
                .collect(Collectors.toList());
        return filteredProfiles;
    }
}
