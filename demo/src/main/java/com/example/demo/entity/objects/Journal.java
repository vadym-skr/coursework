package com.example.demo.entity.objects;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;

@Getter @Setter
@Entity
@Table(name = "journal")
public class Journal extends AbstractBook  {

    @NotBlank(message = "cannot be blank!")
    @Pattern(regexp = "^[A-zА-яіІїЇ0-9_@+=*&^%$#!\\[\\]\\-\\s]*$", message = "you can use only this symbols: A-z А-я 0-9 _@+=*&^%$#![]")
    @Pattern(regexp = "^[^\\s].*", message = "cannot begin or end with a character 'space'")
    @Pattern(regexp = ".*[^\\s]$", message = "cannot begin or end with a character 'space'")
    @Size(min = 4, max = 40, message = "Size of author is wrong, the name range is from 4 to 40")
    private String publisher;

    @NotNull(message = "Days is wrong, you can write number days 1 to 9999")
    @Min(value = 1, message = "Days is wrong, you can write days from 1 to 9999")
    @Max(value = 9999, message = "Days is wrong, you can write days from 1 to 9999")
    @Column(name = "publishing_term")
    private Integer publishingTerm;

}
