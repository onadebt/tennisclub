package cz.inqool.tennisclub.domain.model;

import jakarta.persistence.*;
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
@Entity
public class Reservation extends BaseEntity {

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Court court;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameType gameType;

    @Column(nullable = false)
    private BigDecimal totalPrice;


    public boolean needsUpdate(LocalDateTime newStart, LocalDateTime newEnd, GameType newGameType) {
        return !this.startTime.equals(newStart) || !this.endTime.equals(newEnd) || !this.gameType.equals(newGameType);
    }

    public void update(LocalDateTime newStart, LocalDateTime newEnd, GameType newGameType, BigDecimal newPrice) {
        this.startTime = newStart;
        this.endTime = newEnd;
        this.gameType = newGameType;
        this.totalPrice = newPrice;
    }
}