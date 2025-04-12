package cz.inqool.tennisclub.dto.court;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtUpdateDto {
    @NotBlank(message = "Court name cannot be blank")
    private String courtName;
    @NotBlank(message = "Court surface type cannot be blank")
    private String surfaceTypeName;
}
