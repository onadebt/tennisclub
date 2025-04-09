package cz.inqool.tennisclub.dto.surfacetype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurfaceTypeDto {
    @NotNull
    private Long id;
    @NotBlank
    private String name;
    @Positive(message = "price must be positive number")
    private BigDecimal pricePerMinute;
}
