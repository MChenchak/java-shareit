package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "text")
    @NotBlank
    private String text;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User author;

    @JoinColumn(name = "item_id")
    @ManyToOne
    private Item item;

    @Column(name = "created")
    private LocalDateTime created;
}
