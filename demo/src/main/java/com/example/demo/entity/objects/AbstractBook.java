package com.example.demo.entity.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AbstractBook implements Serializable {
    @Id
    @Column(name = "book_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "name cannot be blank!")
    @Pattern(regexp = "^[A-zА-яіІїЇ0-9_@+=*&^%$#!\\[\\]\\-\\s]*$", message = "you can use only this symbols: A-z А-я 0-9 _@+=*&^%$#![]")
    @Pattern(regexp = "^[^\\s].*", message = "cannot begin or end with a character 'space'")
    @Pattern(regexp = ".*[^\\s]$", message = "cannot begin or end with a character 'space'")
    @Size(min = 4, max = 40, message = "Size of name is wrong, the name range is from 4 to 40")
    protected String name;

    @NotBlank(message = "cannot be blank!")
    @Pattern(regexp = "^[A-zА-яіІїЇ0-9_@+=*&^%$#!\\[\\]\\-\\s]*$", message = "you can use only this symbols: A-z А-я 0-9 _@+=*&^%$#![]")
    @Pattern(regexp = "^[^\\s].*", message = "cannot begin or end with a character 'space'")
    @Pattern(regexp = ".*[^\\s]$", message = "cannot begin or end with a character 'space'")
    @Size(min = 4, max = 40, message = "Size of country is wrong, the name range is from 4 to 40")
    private String country;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractBook that = (AbstractBook) o;

        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return 1807971206;
    }
}
