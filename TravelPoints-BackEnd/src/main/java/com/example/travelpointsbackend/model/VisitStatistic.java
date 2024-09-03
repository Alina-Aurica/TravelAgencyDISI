package com.example.travelpointsbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "visitStatistics")
public class VisitStatistic implements Serializable {
    @Serial
    private static final long serialVersionUID = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="statistic_id", nullable=false)
    private Long statisticId;
    @Column(name="visit_date", nullable=false)
    private Timestamp visitDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "attraction_id", referencedColumnName = "attraction_id")
    private TouristAttraction touristAttraction;

    // I may not need initialization from here
    public VisitStatistic(){
        this.visitDate = new Timestamp(System.currentTimeMillis());
    }

}
