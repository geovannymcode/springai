package com.geovannycode.view;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;

@Route("image-analyzer")
@Menu(title = "Image Analyzer", order = 4)
public class MultiModalityView extends VerticalLayout {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8080/multis/upload";

    public MultiModalityView() {
        setSizeFull();

        // Componente para la carga de imagen
        Upload upload = new Upload();
        upload.setMaxFiles(1);
        upload.setAcceptedFileTypes("image/jpeg", "image/png");
        upload.setMaxFileSize(5 * 1024 * 1024); // Límite de 5 MB para la imagen

        // Contenedor de bytes para almacenar la imagen cargada
        ByteArrayOutputStream uploadBuffer = new ByteArrayOutputStream();

        upload.setReceiver((fileName, mimeType) -> {
            uploadBuffer.reset();
            return uploadBuffer;
        });

        Image imageDisplay = new Image();
        imageDisplay.setWidth("400px");
        imageDisplay.setHeight("400px");

        TextArea descriptionArea = new TextArea("Generated Description:");
        descriptionArea.setWidth("400px");
        descriptionArea.setHeight("100px");
        descriptionArea.setReadOnly(true);

        upload.addSucceededListener(event -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                body.add("image", new ByteArrayResource(uploadBuffer.toByteArray()) {
                    @Override
                    public String getFilename() {
                        return event.getFileName();
                    }
                });

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                // Envía la solicitud POST al backend
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(backendUrl, requestEntity, String.class);

                if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                    String imageUrl = responseEntity.getBody();
                    imageDisplay.setSrc(imageUrl);
                    descriptionArea.setValue("La IA generó la caricatura basada en la descripción.");
                } else {
                    Notification.show("Error: Failed to analyze image.");
                }
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage());
            }
        });

        add(upload, descriptionArea, imageDisplay);
    }
}
