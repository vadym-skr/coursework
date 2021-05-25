package com.example.demo.entity.objects;

import com.example.demo.entity.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "books")
public class Book {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Size(min = 4, max = 40, message = "Size of name is wrong, the name range is from 4 to 40")
    private String name;
    @Size(min = 4, max = 40, message = "Size of author is wrong, the name range is from 4 to 40")
    private String author;
    @NotNull(message = "Number is wrong, you can write number from 1 to 999")
    @Min(value = 1, message = "Number is wrong, you can write number from 1 to 999")
    @Max(value = 999, message = "Number is wrong, you can write number from 1 to 999")
    private Integer pages;
    @Size(min = 4, max = 40, message = "Size of country is wrong, the name range is from 4 to 40")
    private String country;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Genre> genres = new HashSet<>();

    private String description;
}
