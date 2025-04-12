package cz.inqool.tennisclub.domain.service;

import cz.inqool.tennisclub.domain.model.Court;
import cz.inqool.tennisclub.domain.model.GameType;
import cz.inqool.tennisclub.domain.model.SurfaceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorServiceTest {

    private PriceCalculatorService priceCalculatorService;

    @BeforeEach
    void setUp() {
        priceCalculatorService = new PriceCalculatorService();
    }

    @Test
    void calculate_ShouldReturnCorrectPrice_ForStandardGame() {
        // Arrange
        Court court = createCourt(BigDecimal.valueOf(2.5));
        GameType gameType = GameType.SINGLES;
        LocalDateTime start = LocalDateTime.of(2025, 11, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 11, 15, 11, 0);

        // Act
        BigDecimal price = priceCalculatorService.calculate(court, gameType, start, end);

        // Assert
        assertEquals(new BigDecimal("150.00"), price);
    }

    @Test
    void calculate_ShouldReturnCorrectPrice_ForDoublesGame() {
        // Arrange
        Court court = createCourt(BigDecimal.valueOf(3.0));
        GameType gameType = GameType.DOUBLES;
        LocalDateTime start = LocalDateTime.of(2025, 11, 15, 9, 0);
        LocalDateTime end = LocalDateTime.of(2025, 11, 15, 10, 30); // 90 minutes

        // Act
        BigDecimal price = priceCalculatorService.calculate(court, gameType, start, end);

        // Assert
        assertEquals(new BigDecimal("405.00"), price);
    }

    @Test
    void calculate_ShouldReturnZero_ForZeroDuration() {
        // Arrange
        Court court = createCourt(BigDecimal.valueOf(5.0));
        GameType gameType = GameType.SINGLES;
        LocalDateTime start = LocalDateTime.of(2025, 11, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 11, 15, 10, 0);

        // Act
        BigDecimal price = priceCalculatorService.calculate(court, gameType, start, end);

        // Assert
        assertEquals(new BigDecimal("0.00"), price);
    }

    @Test
    void calculate_ShouldHandleFractionalMinutes() {
        // Arrange
        Court court = createCourt(BigDecimal.valueOf(4.0));
        GameType gameType = GameType.SINGLES;
        LocalDateTime start = LocalDateTime.of(2025, 11, 15, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 11, 15, 10, 30);

        // Act
        BigDecimal price = priceCalculatorService.calculate(court, gameType, start, end);

        // Assert
        assertEquals(new BigDecimal("120.00"), price);
    }

    private Court createCourt(BigDecimal pricePerMinute) {
        SurfaceType surfaceType = new SurfaceType();
        surfaceType.setPricePerMinute(pricePerMinute);

        Court court = new Court();
        court.setSurfaceType(surfaceType);

        return court;
    }
}
