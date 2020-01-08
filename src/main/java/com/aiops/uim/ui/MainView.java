package com.aiops.uim.ui;

import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.serviceclient.UIMActionListener;
import com.aiops.uim.mcs.services.TemplateService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;


@Route("mcs")
public class MainView extends VerticalLayout implements UIMActionListener{	
	
	
	//TODO: Need to get this information from ecosystem
	private static UIMInstance uimInstance = new UIMInstance("http", "kp642490-ump-e4", 80, "administrator", "N0tallowed");
	//private UIMInstance uimInstance = new UIMInstance("http", "10.17.175.145", 80, "administrator", "1qaz2wsx");

	ITemplateService templateService = new TemplateService(uimInstance);
	private String devId = null;
	SplitLayout mainLayout;
	SplitLayout monitoringLayout;
	
    public MainView() {  
    	this.setHeight("100%");
    	//BasicLayoutView(); 
    	mainLayout = new SplitLayout();
    	createMainLayout(mainLayout);    	    	
    }
    
    //Create main layout and add tabs to it
    private void createMainLayout(SplitLayout mainLayout) {
    	
    	Tabs tabs = createTabs();
    	mainLayout.setOrientation(Orientation.VERTICAL);
    	mainLayout.setSplitterPosition(5);
    	mainLayout.addToPrimary(tabs);
    	//mainLayout.setPrimaryStyle("width", "100%");
    	mainLayout.setWidthFull();
    	mainLayout.setHeightFull();
    	add(mainLayout);
    	
    	tabs.addSelectedChangeListener(event -> {    	    
    	    Component selectedPage = tabs.getSelectedTab();
    	    String tabName = ((Tab) selectedPage).getLabel();
    	    selectedPage.setVisible(true);
    	    
    	    if(tabName.equals("Dashboard")) {
    	    	mainLayout.addToSecondary(new Label(tabName));
    	    }
    	    if(tabName.equals("Templates")) {
    	    	monitoringLayout = new SplitLayout();
            	
            	monitoringLayout.setOrientation(Orientation.HORIZONTAL);
            	monitoringLayout.setSplitterPosition(25);
            	monitoringLayout.addToPrimary(new MCSTemplateTreeView(templateService, "1", this));   
            	monitoringLayout.addToSecondary(new FormLayout());
            	
            	mainLayout.addToSecondary(monitoringLayout);
            	mainLayout.setPrimaryStyle("height", "5%");
    	    	mainLayout.setSecondaryStyle("height", "95%");
    	    }
    	    else if(tabName.equals("Profiles")) {    	    	
    	    	mainLayout.addToSecondary(new MCSProfilesGridView());
    	    	mainLayout.setPrimaryStyle("height", "5%");
    	    	mainLayout.setSecondaryStyle("height", "95%");
//    	    	tabs.getUI().ifPresent(ui ->
//    	           ui.navigate("profiles"));
    	    	
    	    }    	    
    	});    	
    }
    
    //Create tabs
    private Tabs createTabs() {
    	Tab tabDashboard = new Tab(VaadinIcon.HOME.create(), new Span("DashBoard")); 
    	//Tab tabDashboard = new Tab("DashBoard");
    	Tab tabTemplate = new Tab("Templates");
    	Tab tabProfile = new Tab("Profiles");   
    	//tabDashboard.add
    	tabDashboard.setClassName("tab");
    	tabTemplate.setClassName("tab");
    	tabProfile.setClassName("tab");   
    	
    	Tabs tabs = new Tabs(tabDashboard, tabTemplate, tabProfile);
    	tabs.setFlexGrowForEnclosedTabs(1);
    	tabs.setWidthFull();
    	tabs.setHeight("5%");
    	return tabs;
    }  
    
   
    
    public void BasicLayoutView() {
    	   // Instantiate layouts
    	   HorizontalLayout header = new HorizontalLayout(new Label("One"));
    	   VerticalLayout navBar = new VerticalLayout(new Label("Teo"));
    	   VerticalLayout content = new VerticalLayout(new Label("Three"));
    	   HorizontalLayout center = new HorizontalLayout(new Label("Four"));
    	   HorizontalLayout footer = new HorizontalLayout(new Label("Five"));

    	   // Configure layouts
    	   setSizeFull();
    	   setPadding(false);
    	   setSpacing(false);
    	   header.setWidth("100%");
    	   header.setPadding(true);
    	   center.setWidth("100%");
    	   navBar.setWidth("20%");
    	   content.setWidth("80%");
    	   footer.setWidth("100%");
    	   footer.setPadding(true);

    	   // Compose layout
    	   center.add(navBar, content);
    	   center.setFlexGrow(1, navBar);
    	   add(header, center, footer);
    	   expand(center);
    	 }

	@Override
	public void selectionChanged(String newData) {
		
		//Update UI as per new template ID
		int templateId = Integer.parseInt(newData);				
		try {
			monitoringLayout.addToSecondary(new MCSTemplateFieldsView(templateService, templateId));
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		monitoringLayout.setSplitterPosition(25);
	}

	public static UIMInstance getUimInstance() {
		return uimInstance;
	}
	
	
}
