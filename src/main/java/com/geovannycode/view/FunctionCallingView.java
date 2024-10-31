package com.geovannycode.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Route("book-info")
@Menu(title = "Function Calling", order = 2)
public class FunctionCallingView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8080/functions/book";

    public FunctionCallingView() {
        setSizeFull();

        // Campo de texto para ingresar el nombre del libro
        TextField bookNameInput = new TextField("Enter the book name:");
        bookNameInput.setWidth("400px");

        // Botón de envío
        Button sendButton = new Button("Get Book Info");
        Button clearButton = new Button("Clear");

        // Área de texto para mostrar la respuesta
        TextArea resultArea = new TextArea("Book Information:");
        resultArea.setWidth("600px");
        resultArea.setHeight("300px");
        resultArea.setReadOnly(true); // Solo lectura para mostrar la respuesta

        HorizontalLayout buttonLayout = new HorizontalLayout(sendButton, clearButton);

        sendButton.addClickListener(click -> {
            String bookName = bookNameInput.getValue();

            // Construye la URL con el parámetro del libro
            String url = backendUrl + "?bookName=" + bookName;

            // Llama al backend para obtener la información del libro
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                resultArea.setValue(responseEntity.getBody());  // Muestra la respuesta en el área de texto
            } else {
                resultArea.setValue("Error: Failed to get book information.");
            }
        });

        // Botón para limpiar el área de respuesta
        clearButton.addClickListener(click -> {
            resultArea.clear();
            bookNameInput.clear();
        });

        add(bookNameInput, buttonLayout, resultArea);
    }
}
