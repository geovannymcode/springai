package com.geovannycode.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseDTO<T> {
    private int status;
    private String message;
    private T data;
}
