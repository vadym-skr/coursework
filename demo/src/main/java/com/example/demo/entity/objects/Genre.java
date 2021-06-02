package com.example.demo.entity.objects;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @Column(name = "genre_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "genres")
    private Set<Book> books = new HashSet<>();
}
