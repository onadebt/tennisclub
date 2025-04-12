package cz.inqool.tennisclub.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.inqool.tennisclub.domain.model.GameType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationUpdateDto {
    @NotNull(message = "Start time cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;

    @NotNull(message = "Game type cannot be null")
    private GameType gameType;
}
