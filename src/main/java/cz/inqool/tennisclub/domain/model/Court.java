package cz.inqool.tennisclub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Court extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String courtName;

    @ManyToOne
    @JoinColumn(nullable = false)
    private SurfaceType surfaceType;
}

