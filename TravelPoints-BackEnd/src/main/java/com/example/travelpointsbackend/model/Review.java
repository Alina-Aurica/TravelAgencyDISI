package com.example.travelpointsbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "reviews")
public class Review implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id", nullable=false)
    private Long reviewId;
    @Column(name="review_text", nullable=false)
    private String reviewText;
    @Column(name="rating", nullable=false)
    private Integer rating;
    @Column(name="created_at", nullable=false)
    private Timestamp createdAt;
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @JsonBackReference
    @ManyToOne(optional = false)
    @JoinColumn(name = "attraction_id", referencedColumnName = "attraction_id")
    private TouristAttraction touristAttraction;

    public Review(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }
}
