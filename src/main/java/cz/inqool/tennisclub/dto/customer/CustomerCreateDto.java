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
public class CustomerCreateDto {
    @NotBlank
    private String name;
    @Pattern(regexp = "^(\\+420)? ?[1-9][0-9]{2} ?[0-9]{3} ?[0-9]{3}$")
    private String phoneNumber;
}
