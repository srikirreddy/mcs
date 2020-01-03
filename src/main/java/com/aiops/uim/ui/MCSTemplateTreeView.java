package com.aiops.uim.ui;

import java.util.Set;

import com.aiops.uim.mcs.serviceclient.ITemplateService;
import com.aiops.uim.mcs.serviceclient.UIMActionListener;
import com.nimsoft.selfservice.v2.model.Template;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Route;

@Route("templates")
public class MCSTemplateTreeView extends VerticalLayout {

	//Read data from here
	private ITemplateService service = null;
	private String devId = null;
	//private UIMActionListener listener = null;

	public MCSTemplateTreeView(ITemplateService service, String devId, UIMActionListener listner) {

		this.service = service;
		this.devId = devId;

		TreeGrid<Template> tree = new TreeGrid<>();
		tree.setSelectionMode(SelectionMode.SINGLE);

		tree.setItems(service.getAllTemplatesByDevice(Long.parseLong(devId)));

		tree.addHierarchyColumn(Template::getTemplateName).setHeader("Template Name");
		tree.addColumn(Template::getProbe).setHeader("Probe");

		tree.addSelectionListener(event -> {
			Set<Template> selectedItems = event.getAllSelectedItems();

			Template t = (selectedItems.size()>0) ? selectedItems.iterator().next() : null;

			if (listner!=null) {
				listner.selectionChanged("" + t.getTemplateId());
				//new MCSTemplateFieldsView(service,t.getTemplateId());
			}
		});

		add(tree);
	}	
}


