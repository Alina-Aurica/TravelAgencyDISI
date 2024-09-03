package com.example.travelpointsbackend.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "touristAttractions")
public class TouristAttraction implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="attraction_id", nullable=false)
    private Long attractionId;
    @Column(name="name", nullable=false)
    private String name;
    @Column(name="location", nullable=false)
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(name="category", nullable=false)
    private Category category;
    @Column(name="created_at", nullable = false)
    private Timestamp createdAt;
    @JsonManagedReference
    @OneToOne(mappedBy = "touristAttraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttractionDetail attractionDetail;
    @ManyToMany(mappedBy = "touristAttractionList")
    private List<UserWishlist> userWishlistList;
    @JsonManagedReference
    @OneToMany(mappedBy = "touristAttraction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviewList;
    @OneToMany(mappedBy = "touristAttraction", cascade = CascadeType.ALL)
    private List<VisitStatistic> visitStatisticList;


    public TouristAttraction(){
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString(){
        return "Tourist Attraction: " + this.attractionId + " " +
                this.name + " " + this.location + " " + this.category + " " +
                this.createdAt + " " + this.attractionDetail.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TouristAttraction myObject = (TouristAttraction) obj;
        return Objects.equals(this.attractionId, myObject.attractionId);
    }
}
