package prjnightsky.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public StarMap() {
    }

    public StarMap(String location, LocalTime time, User user, LocalDate date, String mapStyle, String colorScheme, String size, boolean showConstellations, boolean showGrid, boolean showLabels, String customMessage) {
        this.location = location;
        this.time = time;
        this.user = user;
        this.date = date;
        this.mapStyle = mapStyle;
        this.colorScheme = colorScheme;
        this.size = size;
        this.showConstellations = showConstellations;
        this.showGrid = showGrid;
        this.showLabels = showLabels;
        this.customMessage = customMessage;
    }
}
