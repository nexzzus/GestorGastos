package com.nexzus.gestiongastos.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
        private int status;
        private String message;
        private String error;
        private String path;
        private LocalDateTime timespamp;
        private Map<String, String> fieldErrors;
}
