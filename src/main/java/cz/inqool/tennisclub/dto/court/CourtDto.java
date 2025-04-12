package cz.inqool.tennisclub.dto.court;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtDto {
    private Long id;
    private String courtName;
    private String surfaceTypeName;
}
