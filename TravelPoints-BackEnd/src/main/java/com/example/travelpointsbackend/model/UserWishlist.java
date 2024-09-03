package com.example.travelpointsbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "userWishlists")
public class UserWishlist implements Serializable {
    @Serial
    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="wishlist_id", nullable=false)
    private Long wishlistId;
    @Column(name="added_at", nullable=false)
    private Timestamp addedAt;
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;
    @ManyToMany()
    @JoinTable(name = "wishlist_attractions",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "attraction_id")
    )
    private List<TouristAttraction> touristAttractionList;

    public UserWishlist(){
        this.addedAt = new Timestamp(System.currentTimeMillis());
    }

}
