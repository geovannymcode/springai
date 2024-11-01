package com.geovannycode.view;

import com.geovannycode.dto.ResponseDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Route("image-generator")
@Menu(title = "Image Model", order = 3)
public class ImageGeneratorView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8080/images/generate";

    public ImageGeneratorView() {
        setSizeFull();

        // Campo de texto para ingresar la descripción de la imagen
        TextArea descriptionInput = new TextArea("Enter image description:");
        descriptionInput.setWidth("400px");

        // Botón para generar la imagen
        Button generateButton = new Button("Generate Image");

        // Contenedor de la imagen
        Image imageDisplay = new Image();
        imageDisplay.setWidth("400px"); // Ajusta el ancho de la imagen
        imageDisplay.setHeight("400px");

        generateButton.addClickListener(click -> {
            String description = descriptionInput.getValue();
            String url = backendUrl + "?param=" + description;

            // Llamada al backend para obtener la URL de la imagen
            ResponseEntity<ResponseDTO<String>> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<ResponseDTO<String>>() {}
            );

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                String imageUrl = responseEntity.getBody().getData(); // URL de la imagen generada
                imageDisplay.setSrc(imageUrl); // Mostrar la imagen en el componente Image
            } else {
                Notification.show("Error: Failed to generate image.");
            }
        });

        add(descriptionInput, generateButton, imageDisplay);
    }
}