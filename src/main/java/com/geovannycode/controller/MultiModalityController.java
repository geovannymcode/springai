package com.geovannycode.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/multis")
@RequiredArgsConstructor
public class MultiModalityController {

    private final OpenAiChatModel openAiChatModel;
    private final OpenAiImageModel openAiImageModel;
    
    @PostMapping("/upload")
    public String multiModalityUpload(@RequestParam("image") MultipartFile imageFile) throws Exception{
        UserMessage userMessage = new UserMessage(
                "Explicame que ves en esta imagen?",
                List.of(new Media(MimeTypeUtils.IMAGE_JPEG, new ByteArrayResource(imageFile.getBytes())))
        );

        ChatResponse response = openAiChatModel.call(new Prompt(List.of(userMessage)));
        String description = response.getResult().getOutput().getContent();

        return openAiImageModel.call(new ImagePrompt("Generame una caricatura de esta descripcion: " + description,
                OpenAiImageOptions.builder()
                        .withModel("dall-e-3")
                        .withQuality("standard")
                        .withN(1)
                        .withHeight(1024)
                        .withWidth(1024)
                        .build()
        )).getResult().getOutput().getUrl();
    }
}
