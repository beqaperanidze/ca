package entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "star maps")
@Getter
@Setter
public class StarMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String location;
    private LocalDate date;
    private LocalTime time;
    private String mapStyle;
    private String colorScheme;
    private String size;
    private boolean showConstellations;
    private boolean showGrid;
    private boolean showLabels;
    private String customMessage;

    public StarMap() {
    }
}
