package cz.inqool.tennisclub.domain.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public enum GameType {
    SINGLES(1.0F),
    DOUBLES(1.5F);

    private final BigDecimal priceMultiplier;

    GameType(float priceMultiplier) {
        this.priceMultiplier = BigDecimal.valueOf(priceMultiplier);
    }
}
