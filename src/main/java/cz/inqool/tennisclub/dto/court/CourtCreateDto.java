package cz.inqool.tennisclub.dto.court;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtCreateDto {
    @NotBlank(message = "Court name cannot be blank")
    private String courtName;
    @NotNull(message = "Surface type name cannot be null")
    private String surfaceTypeName;
}
