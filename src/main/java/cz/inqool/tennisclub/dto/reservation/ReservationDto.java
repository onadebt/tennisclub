package cz.inqool.tennisclub.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.inqool.tennisclub.domain.model.GameType;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Court ID cannot be null")
    private Long courtId;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Start time cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "Game type cannot be null")
    private GameType gameType;

    @NotNull(message = "Price cannot be null")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal totalPrice;
}
