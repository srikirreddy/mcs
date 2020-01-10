package com.aiops.uim.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.aiops.uim.mcs.serviceclient.IProfileService;
import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.services.ProfileService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.nimsoft.selfservice.v2.model.RawProfile;
import com.vaadin.flow.component.notification.Notification;
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
    	
    	 // basic tree setup
        TreeGrid<RawProfile> treeGrid = new TreeGrid<>();
    	
    	TextField filterText = new TextField();
    	filterText.setPlaceholder("Filter by profile name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.EAGER);
        filterText.addValueChangeListener(e -> updateList(treeGrid, cs_id, filterText.getValue()));
    	
       
        //addComponentAsFirst(treeGrid);
        treeGrid.addHierarchyColumn(RawProfile::getProfileName).setHeader("Profile Name");
        treeGrid.addColumn(RawProfile::getStatus).setHeader("Status");//.setCaption("Last Modified");
        treeGrid.addColumn(RawProfile::getTemplateId).setHeader("Template Id");//.setCaption("Hours Done");
        

        // some listeners for interaction
//        treeGrid.addExpandListener(event -> Notification.show("hi"));
//        treeGrid.addCollapseListener(event -> Notification.show("hihihhhi"));
        
//        treeGrid.addCollapseListener(event -> Notification
//                .show("Project '" ));//+ event.getCollapsedItem().getName() + "' collapsed.", Notification.Type.TRAY_NOTIFICATION));
//        treeGrid.addExpandListener(event -> Notification
//                .show("Project '" ));//+ event.getExpandedItem().getName() + "' expanded.", Notification.Type.TRAY_NOTIFICATION));


        // add the list of root projects and specify a provider of sub-projects
        updateList(treeGrid, cs_id, filterText.getValue());
        //treeGrid.setItems(profiles, RawProfile::getSubProfiles);
        treeGrid.setPageSize(2);
        
        //treeGrid.setItems(generateProjectsForYears(2010, 2016), Project::getSubProjects);
        add(filterText, treeGrid);
    }
    
    public void updateList(TreeGrid treeGrid, int cs_id, String profileNameSearch) {
    	profileService = new ProfileService(MainView.getUimInstance());
        List<RawProfile> profiles = profileService.getAllProfilesForDevice(cs_id, profileNameSearch);
        treeGrid.setItems(profiles);
    }

    // generate some random projects
    private List<Project> generateProjectsForYears(int startYear, int endYear) {
        List<Project> projects = new ArrayList<>();

        for (int year = startYear; year <= endYear; year++) {
            Project yearProject = new Project("Year " + year);

            for (int i = 1; i < 2 + random.nextInt(5); i++) {
                Project customerProject = new Project("Customer Project " + i);
                customerProject.setSubProjects(Arrays.asList(
                        new LeafProject("Implementation", random.nextInt(100), year),
                        new LeafProject("Planning", random.nextInt(10), year),
                        new LeafProject("Prototyping", random.nextInt(20), year)));
                yearProject.addSubProject(customerProject);
            }
            projects.add(yearProject);
        }
        return projects;
    }


    // basic parent (or intermediate child) bean used for easy binding
    class Project {
        private List<Project> subProjects = new ArrayList<>();
        private String name;

        public Project(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public List<Project> getSubProjects() {
            return subProjects;
        }

        public void setSubProjects(List<Project> subProjects) {
            this.subProjects = subProjects;
        }

        public void addSubProject(Project subProject) {
            subProjects.add(subProject);
        }

        public int getHoursDone() {
            return getSubProjects().stream().map(project -> project.getHoursDone()).reduce(0, Integer::sum);
        }

        public Date getLastModified() {
            return getSubProjects().stream().map(project -> project.getLastModified()).max(Date::compareTo).orElse(null);
        }
    }


    // basic final child (can not have other children) bean used for easy binding
    class LeafProject extends Project {
        private int hoursDone;
        private Date lastModified;

        public LeafProject(String name, int hoursDone, int year) {
            super(name);
            this.hoursDone = hoursDone;
            lastModified = new Date(year - 1900, random.nextInt(12), random.nextInt(10));
        }

        @Override
        public int getHoursDone() {
            return hoursDone;
        }

        @Override
        public Date getLastModified() {
            return lastModified;
        }
    }
}
