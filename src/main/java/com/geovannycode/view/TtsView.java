package com.geovannycode.view;

import com.vaadin.flow.component.HtmlComponent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;

@Route("text-to-speech")
@Menu(title = "Text to Speech", order = 5)
public class TtsView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl = "http://localhost:8080/tts";

    public TtsView() {
        setSizeFull();

        TextArea messageInput = new TextArea("Enter the message to convert to speech:");
        messageInput.setWidth("400px");

        Button generateAudioButton = new Button("Generate Audio");

        // Placeholder for audio player
        HtmlComponent audioPlayer = new HtmlComponent("audio");
        audioPlayer.getElement().setAttribute("controls", true);

        generateAudioButton.addClickListener(click -> {
            String message = messageInput.getValue();
            String url = backendUrl + "?message=" + message;

            try {
                ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                        url,
                        org.springframework.http.HttpMethod.GET,
                        null,
                        byte[].class
                );

                if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                    StreamResource streamResource = new StreamResource("tts_audio.mp3", () ->
                            new ByteArrayInputStream(responseEntity.getBody()));
                    audioPlayer.getElement().setAttribute("src", streamResource);
                } else {
                    Notification.show("Error: Failed to generate audio.");
                }
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage());
            }
        });

        add(messageInput, generateAudioButton, audioPlayer);
    }
}



