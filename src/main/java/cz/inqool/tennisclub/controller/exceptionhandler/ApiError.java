package cz.inqool.tennisclub.controller.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ApiError {
    private String message;
    private String field;
    private String rejectedValue;
}
