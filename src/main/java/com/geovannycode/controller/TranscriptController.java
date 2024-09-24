package com.geovannycode.controller;

import com.geovannycode.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/transcripts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TranscriptController {

    //Se usa el modelo Whisper
    private final OpenAiAudioTranscriptionModel transcriptionModel;

    @GetMapping("/es")
    public ResponseEntity<String> transcriptES(){
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("es")
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .withTemperature(0f)
                .build();

        ClassPathResource audioFile = new ClassPathResource("/audios/audio.mp4");

        AudioTranscriptionPrompt transcriptionPrompt = new AudioTranscriptionPrompt(audioFile, options);
        AudioTranscriptionResponse response = transcriptionModel.call(transcriptionPrompt);

        return ResponseEntity.ok(response.getResult().getOutput());
    }

    @GetMapping("/en")
    public ResponseEntity<String> transcriptEN(){
        OpenAiAudioTranscriptionOptions options = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("en")
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .withTemperature(0f)
                .build();

        ClassPathResource audioFile = new ClassPathResource("/audios/audio.mp4");

        AudioTranscriptionPrompt transcriptionPrompt = new AudioTranscriptionPrompt(audioFile, options);
        AudioTranscriptionResponse response = transcriptionModel.call(transcriptionPrompt);

        return ResponseEntity.ok(response.getResult().getOutput());
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDTO<String>> handleAudioUpload(@RequestParam("audio") MultipartFile audioFile) {
        try {
            String uploadDirPath = "src/main/resources/audios/uploads/";

            Path uploadPath = Paths.get(uploadDirPath);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = "audio_" + System.currentTimeMillis() + ".wav";
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(audioFile.getInputStream(), filePath);

            OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                    .withLanguage("es")
                    .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                    .withTemperature(0f)
                    .build();

            Resource audioFileUploaded = new FileSystemResource(uploadDirPath + fileName);

            AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFileUploaded, transcriptionOptions);
            AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);

            return ResponseEntity.ok(new ResponseDTO<>(200, "success", response.getResult().getOutput()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
