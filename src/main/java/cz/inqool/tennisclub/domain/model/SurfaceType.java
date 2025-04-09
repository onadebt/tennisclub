package cz.inqool.tennisclub.domain.model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class SurfaceType extends BaseEntity {
    private String name;
    private BigDecimal pricePerMinute;
}

