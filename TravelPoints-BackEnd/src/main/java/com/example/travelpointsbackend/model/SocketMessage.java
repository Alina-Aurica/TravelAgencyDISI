package com.example.travelpointsbackend.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessage {
    private Long userId;
    private String nameAttraction;
    private String offers;
}
