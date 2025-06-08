package com.academy.e_commerce.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private boolean flag; // Two values: true means success, false means not success
    private Integer code; // Status code. e.g., 200
    private String message; // Response message
    private Object data; // The response payload

    Result(boolean flag, Integer code, String message){
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
}
