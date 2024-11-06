package com.geovannycode.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tts")
@RequiredArgsConstructor
public class TtsController {

    private final OpenAiAudioSpeechModel openAiAudioSpeechModel;

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> tts(@RequestParam("message") String message) throws Exception {
        OpenAiAudioSpeechOptions speechOptions = OpenAiAudioSpeechOptions.builder()
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
                .withSpeed(1.0f)
                .withModel(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
                .build();

        SpeechPrompt prompt = new SpeechPrompt(message, speechOptions);
        SpeechResponse response = openAiAudioSpeechModel.call(prompt);

        byte[] responseBytes = response.getResult().getOutput();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"tts_audio.mp3\"")
                .body(responseBytes);
    }

}
