package com.aiops.uim.ui;

import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.serviceclient.UIMActionListener;
import com.aiops.uim.mcs.services.TemplateService;
import com.aiops.uim.mcs.utils.UIMInstance;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout.Orientation;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;


@Route("")
public class MainView extends HorizontalLayout implements UIMActionListener{	
	
	
	//TODO: Need to get this information from ecosystem
	private static UIMInstance uimInstance = new UIMInstance("http", "kp642490-ump-e4", 80, "administrator", "N0tallowed");
	//private UIMInstance uimInstance = new UIMInstance("http", "10.17.175.145", 80, "administrator", "1qaz2wsx");

	ITemplateService templateService = new TemplateService(uimInstance);
	private String devId = null;
	SplitLayout mainLayout;
	SplitLayout monitoringLayout;
	VerticalLayout layout;
	
	int cs_id = 1;
	
    public MainView() {  
    	layout = new VerticalLayout();
    	layout.setWidth("12%");
    	layout.setHeightFull();
    	layout.setSpacing(true);
    	setHeightFull();
    	VerticalLayout layoutVert = new VerticalLayout();
//    	VerticalMenu vm = new VerticalMenu(new Section(new H1("Inbox"),
//				   new Section(new H1("Profile")),
//				   new Section(new H1("Friends")),
//				   new Section(new H1("Messages")),
//				   new Section(new H1("Settings")));
    	
//    	Icon icon = new Icon(VaadinIcon.USER);
//    	icon.getStyle().set("cursor", "pointer");
    	MenuBar menuBar = new MenuBar();
    	menuBar.addThemeVariants(MenuBarVariant.MATERIAL_CONTAINED);
//    	MenuItem profile = menuBar.addItem("Users");
//    	profile.addComponentAsFirst(new Icon(VaadinIcon.USER));
    	Button usersBtn = new Button(new Icon(VaadinIcon.USER));
    	usersBtn.setText("MCS Admin");
    	Button signOutBtn = new Button(new Icon(VaadinIcon.SIGN_OUT));
    	signOutBtn.setText("SignOut");
//    	Text selected = new Text("");
//    	Div message = new Div(new Text("Selected: "), selected);
//
//    	MenuItem project = menuBar.addItem("Project");
//    	MenuItem account = menuBar.addItem("Account");
//    	menuBar.addItem("Sign Out", e -> selected.setText("Sign Out"));
//    	SubMenu projectSubMenu = project.getSubMenu();
//    	project.getSubMenu().add(new Hr());
//    	MenuItem users = projectSubMenu.addItem("Users");
//    	MenuItem billing = projectSubMenu.addItem("Billing");
    	// layout.add(project);
    	
    	
    	layoutVert.add(menuBar);
    	layoutVert.add(usersBtn);
    	layoutVert.add(signOutBtn);
    	// layoutVert.add(but2);
    	//layoutVert.add(icon);
    	layout.add(layoutVert);
    	layout.getElement().getStyle().set("margin-left", "1%");
    	layout.setAlignItems(Alignment.START);
    	// layout.setVerticalComponentAlignment(Alignment.START);
    	//layout.setMargin(true);
    	layout.getElement().getStyle().set("border", "1px solid black");
    	add(layout);
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
    	mainLayout.setWidth("83%");
    	// mainLayout.getElement().getStyle().set("flexShrink", "0");
    	mainLayout.getElement().getStyle().set("margin-left", "0%");
    	mainLayout.getElement().getStyle().set("border", "1px solid black");
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
            	monitoringLayout.addToPrimary(new MCSTemplateTreeView(templateService, cs_id, this));   
            	monitoringLayout.addToSecondary(new FormLayout());
            	
            	mainLayout.addToSecondary(monitoringLayout);
            	mainLayout.setPrimaryStyle("height", "5%");
    	    	mainLayout.setSecondaryStyle("height", "95%");
    	    }
    	    else if(tabName.equals("Profiles")) {    	    	
    	    	mainLayout.addToSecondary(new MCSProfilesGridView(cs_id));
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
			monitoringLayout.addToSecondary(new MCSTemplateFieldsView(templateService, templateId, cs_id));
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
