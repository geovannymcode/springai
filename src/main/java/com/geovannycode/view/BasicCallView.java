package com.geovannycode.view;

import com.geovannycode.dto.ResponseDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@Route("chat")
@Menu(title = "Basic Calling", order = 1)
public class BasicCallView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();  // Cliente para hacer solicitudes HTTP
    private final String backendUrl = "http://localhost:8080/chats/generateConversation";  // URL de tu controlador

    public BasicCallView() {
        setSizeFull();

        TextArea messageInput = new TextArea("Enter your message:");
        messageInput.setWidth("600px"); // Ajusta el ancho del área de texto
        messageInput.setHeight("150px"); // Ajusta la altura del área de texto

        Button sendButton = new Button("Send");
        Button clearButton = new Button("Clear");

        // Area para mostrar la respuesta
        VerticalLayout messages = new VerticalLayout();
        messages.setWidth("600px");

        // Layout horizontal para los botones
        HorizontalLayout buttonLayout = new HorizontalLayout(sendButton, clearButton);

        sendButton.addClickListener(click -> {
            String message = messageInput.getValue();
            // Usando ParameterizedTypeReference para manejar el tipo de datos genérico
            ResponseEntity<ResponseDTO<String>> responseEntity = restTemplate.exchange(
                    backendUrl + "?message=" + message,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ResponseDTO<String>>() {}
            );


            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                String response = responseEntity.getBody().getData();  // Extraer el mensaje de respuesta
               // messages.add(new Paragraph("Bot: " + response));
                Div responseContainer = new Div();
                responseContainer.getStyle()
                        .set("padding", "10px")
                        .set("margin-top", "10px")
                        .set("background-color", "#f1f3f4")
                        .set("border-radius", "8px")
                        .set("border", "1px solid #e0e0e0");
                responseContainer.add(new Paragraph("Bot: " + response));
                messages.add(responseContainer);
            } else {
                messages.add(new Paragraph("Error: Failed to get response from backend"));
            }
        });

        // Botón para limpiar el área de mensajes
        clearButton.addClickListener(click -> {
            messages.removeAll();
            messageInput.clear();
        });

        add(messageInput, buttonLayout, messages);
    }
}

