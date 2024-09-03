package com.example.travelpointsbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "attractionDetails")
public class AttractionDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="detail_id", nullable=false)
    private Long detailId;
    @Column(name="description_text", nullable=false)
    private String descriptionText;
    @Column(name="image_path", nullable = false)
    private String imagePath;
    @Column(name="entry_price", nullable=false)
    private Float entryPrice;
    @Column(name="offers", nullable=false)
    private String offers;
    @OneToOne(optional = false)
    @JsonBackReference
    @JoinColumn(name = "attraction_id", referencedColumnName = "attraction_id")
    private TouristAttraction touristAttraction;

    @Override
    public String toString() {
        return this.descriptionText + " " + this.imagePath + " " +
                this.entryPrice + " " + this.offers;
    }

}
