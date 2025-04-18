package cz.inqool.tennisclub.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDto {
    @NotBlank
    private String name;
    @Pattern(regexp = "^(\\+420 ?)?\\d{3} ?\\d{3} ?\\d{3}$")
    private String phoneNumber;
}
