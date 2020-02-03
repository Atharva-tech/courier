package com.krupatek.courier;

import com.krupatek.courier.model.AccountCopy;
import com.krupatek.courier.repository.CompanyRepository;
import com.krupatek.courier.service.*;
import com.krupatek.courier.utils.DateUtils;
import com.krupatek.courier.view.CustomerBillingDetailsForm;
import com.krupatek.courier.view.SystemSettingsForm;
import com.krupatek.courier.view.accountcopy.AccountCopyEditor;
import com.krupatek.courier.view.accountcopy.NewAccountCopyForm;
import com.krupatek.courier.view.clientprofile.ClientProfileEditor;
import com.krupatek.courier.view.rate.RateEntryEditor;
import com.krupatek.courier.view.rate.RateIntEntryEditor;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.springframework.beans.factory.annotation.Autowired;

@Route
@Theme(value = Material.class, variant = Material.DARK)
@PWA(name = "Project Base for Vaadin Flow with Spring", shortName = "Project Base")
public class MainView extends VerticalLayout {
    @Autowired
    AccountCopyService accountCopyService;

    @Autowired
    ClientService clientService;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    DestinationService destinationService;

    @Autowired
    RateMasterService rateMasterService;

    @Autowired
    RateIntMasterService rateIntMasterService;

    @Autowired
    CourierService courierService;

    @Autowired
    DateUtils dateUtils;

    public MainView(@Autowired MessageBean bean) {
        MenuBar menuBar = new MenuBar();
        Div component = new Div();

        menuBar.setOpenOnHover(true);
        Text selected = new Text("");
        Div message = new Div(new Text("Selected: "), selected);

        MenuItem systemSettings = menuBar.addItem("System Settings");
        MenuItem masters = menuBar.addItem("Masters");
        MenuItem billingDetails = menuBar.addItem("Billing Details");
        MenuItem reports = menuBar.addItem("Reports");

        SubMenu systemSettingsSubMenu = systemSettings.getSubMenu();
        systemSettingsSubMenu.addItem("System Settings", e -> {
            component.removeAll();
            component.add(new SystemSettingsForm(companyRepository));
        }  );
        MenuItem users = systemSettingsSubMenu.addItem("Users");
        MenuItem billing = systemSettingsSubMenu.addItem("Billing");

        SubMenu usersSubMenu = users.getSubMenu();
        usersSubMenu.addItem("List", e -> selected.setText("List"));
        usersSubMenu.addItem("Add", e -> selected.setText("Add"));

        SubMenu billingSubMenu = billing.getSubMenu();
        billingSubMenu.addItem("Invoices", e -> selected.setText("Invoices"));
        billingSubMenu.addItem("Balance Events",
                e -> selected.setText("Balance Events"));

        masters.getSubMenu().addItem("Client Profile",
                e -> {
                    component.removeAll();
                    component.add(new ClientProfileEditor(clientService));
                });

        MenuItem rateMaster = masters.getSubMenu().addItem("Rate Master");
        rateMaster.getSubMenu().addItem("Rate Entry", e -> {
            component.removeAll();
            component.add(new RateEntryEditor(clientService, courierService, rateMasterService));
        });
        rateMaster.getSubMenu().addItem("Rate International Entry", e -> {
            component.removeAll();
            component.add(new RateIntEntryEditor(clientService, courierService, rateIntMasterService));
        });

        masters.getSubMenu().addItem("Edit Profile",
                e -> selected.setText("Edit Profile"));
        masters.getSubMenu().addItem("Privacy Settings",
                e -> selected.setText("Privacy Settings"));

        MenuItem accountCopyMenuItem = billingDetails.getSubMenu().addItem("Account Copy");
        accountCopyMenuItem.getSubMenu().addItem("Create Account Copy", e -> {
            component.removeAll();
            component.add(new NewAccountCopyForm(
                    accountCopyService,
                    clientService,
                    destinationService,
                    rateMasterService ,
                    dateUtils,
                    new AccountCopy()));
        } );
        accountCopyMenuItem.getSubMenu().addItem("Edit Account Copy", e -> {
            component.removeAll();
            component.add(new AccountCopyEditor(accountCopyService, clientService, destinationService, rateMasterService,  dateUtils));
        } );
        billingDetails.getSubMenu().addItem("Direct Edition", e -> {
            component.removeAll();
            component.add(new CustomerBillingDetailsForm(accountCopyService, clientService));
        } );

        add(menuBar);
        add(component);
    }

}
