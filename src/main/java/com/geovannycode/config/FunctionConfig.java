package com.geovannycode.config;

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
}
