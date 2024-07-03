package com.example.todo_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ErrorResponse {
    private String title;
    private int status;
    private List<String> errors = new ArrayList<>();
}
