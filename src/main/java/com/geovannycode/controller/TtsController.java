package com.geovannycode.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
public class TtsController {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @GetMapping
    public byte[] tts(@RequestParam("message") String message) throws Exception{
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .withSpeed(1.0f) //2.0f
                .withModel(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
                .build();

        SpeechPrompt prompt = new SpeechPrompt(message, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(prompt);

        byte[] responseBytes = response.getResult().getOutput();

        //Crear el directorio si no existe
        Path directory = Paths.get("src/main/resources/audios/tts/");
        if(Files.notExists(directory)){
            Files.createDirectories(directory);
        }

        //Ruta del archivo
        Path filePath = directory.resolve("tts_1.mp3");

        try(ByteArrayInputStream inputStream = new ByteArrayInputStream(responseBytes)){
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }


        return responseBytes;

    }
}
