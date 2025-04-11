package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.GameType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class PriceCalculatorService {
    public BigDecimal calculate(Court court, GameType gameType, LocalDateTime start, LocalDateTime end) {
        BigDecimal duration = BigDecimal.valueOf(Duration.between(start, end).toMinutes());
        return court.getSurfaceType().getPricePerMinute()
                .multiply(duration)
                .multiply(gameType.getPriceMultiplier());
    }
}

