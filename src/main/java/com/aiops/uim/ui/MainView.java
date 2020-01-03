package com.aiops.uim.ui;

import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.serviceclient.UIMActionListener;
import com.aiops.uim.mcs.services.TemplateService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;


@Route("mcs")
public class MainView extends VerticalLayout implements UIMActionListener{	
	
	
	//TODO: Need to get this information from ecosystem
	private UIMInstance uimInstance = new UIMInstance("http", "kp642490-ump-e4", 80, "administrator", "N0tallowed");
	//private UIMInstance uimInstance = new UIMInstance("http", "10.17.175.145", 80, "administrator", "1qaz2wsx");

	ITemplateService templateService = new TemplateService(uimInstance);
	private String devId = null;
	SplitLayout mainLayout;
	SplitLayout monitoringLayout;
	
    public MainView() {  
    	
    	//BasicLayoutView();   
    	mainLayout = new SplitLayout();
    	createMainLayout(mainLayout);    	    	
    }
    
    //Create main layout and add tabs to it
    private void createMainLayout(SplitLayout mainLayout) {
    	
    	Tabs tabs = createTabs();
    	mainLayout.setOrientation(Orientation.VERTICAL);
    	mainLayout.setSplitterPosition(25);
    	mainLayout.addToPrimary(tabs);
    	add(mainLayout);
    	
    	tabs.addSelectedChangeListener(event -> {    	    
    	    Component selectedPage = tabs.getSelectedTab();
    	    String tabName = ((Tab) selectedPage).getLabel();
    	    selectedPage.setVisible(true);
    	    if(tabName.equals("Templates")) {
    	    	monitoringLayout = new SplitLayout();
            	
            	monitoringLayout.setOrientation(Orientation.HORIZONTAL);
            	monitoringLayout.setSplitterPosition(100);
            	monitoringLayout.addToPrimary(new MCSTemplateTreeView(templateService, "1", this));   
            	monitoringLayout.addToSecondary(new Label(""));
            	
            	mainLayout.addToSecondary(monitoringLayout);
    	    }
    	    else {
    	    	mainLayout.addToSecondary(new Label(tabName));
    	    }    	    
    	});    	
    }
    
    //Create tabs
    private Tabs createTabs() {
    	Tab tabDashboard = new Tab("DashBoard");    	 	
    	Tab tabTemplate = new Tab("Templates");
    	Tab tabProfile = new Tab("Profiles");   
    	
    	Tabs tabs = new Tabs(tabDashboard, tabTemplate, tabProfile);
    	tabs.setFlexGrowForEnclosedTabs(1);
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
		monitoringLayout.addToSecondary(new MCSTemplateFieldsView(templateService, templateId));
		monitoringLayout.setSplitterPosition(25);
	}
}
