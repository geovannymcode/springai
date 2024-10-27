package com.geovannycode.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geovannycode.repo.IBookRepo;
import com.geovannycode.service.impl.BookFunctionServiceImpl;
import com.geovannycode.service.impl.MockWeatherService;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FunctionConfig {

    @Bean
    public FunctionCallback weatherFunctionInfo(){
        return FunctionCallbackWrapper.builder(new MockWeatherService())
                .withName("weatherFunction")
                .withDescription("Get the weather in location")
                .withResponseConverter( (response -> "" + response.temp() + response.unit() ))
                .build();
    }

    @Bean
    public FunctionCallback bookInfoFunction(IBookRepo repo){
        return FunctionCallbackWrapper.builder(new BookFunctionServiceImpl(repo))
                .withName("BookInfo")
                .withDescription("Get book info from book name")
                .withResponseConverter( (response -> "" + response.books()))
                /*.withResponseConverter(response -> {
                    try {
                        return new ObjectMapper().writeValueAsString(response.books());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })*/
                .build();
    }
}
