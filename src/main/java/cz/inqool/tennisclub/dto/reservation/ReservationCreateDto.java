package cz.inqool.tennisclub.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import cz.inqool.tennisclub.domain.model.GameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationCreateDto {
    @NotBlank(message = "Court name cannot be blank")
    private String courtName;

    @NotNull(message = "Customer phone number cannot be null")
    @Pattern(
            regexp = "^(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?$",
            message = "Phone number must be 9 digits (e.g., +420 777 123 456)\""
    )
    private String customerPhone;

    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    @NotNull(message = "Game type cannot be blank")
    private GameType gameType;

    @NotNull(message = "Reservation name cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;
}