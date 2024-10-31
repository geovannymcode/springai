package com.geovannycode.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)  // Ruta principal
public class HomeView extends VerticalLayout {
    public HomeView() {
        add(new H1("Welcome to Enterprise AI Jug Guatemala 2024"));
    }
}
