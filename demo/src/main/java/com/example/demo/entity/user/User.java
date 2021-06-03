package com.example.demo.entity.user;

import com.example.demo.entity.objects.Book;
import com.example.demo.entity.objects.BookRating;
import com.example.demo.entity.objects.Journal;
import com.example.demo.entity.objects.JournalRating;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Getter @Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank
    @Size(min = 4, max = 40, message = "Size of name is wrong, the name range is from 4 to 40")
    private String username;
    @NotBlank
    @Size(min = 4, max = 64, message = "Size of password is wrong, the password range is from 4 to 64")
    private String password;
    @Email(message = "Format of email is wrong")
    private String email;
    private boolean enabled;
    @Transient
    private String activatedCode;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "favorite_books_for_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> favoriteBooks = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<BookRating> bookRatings = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "favorite_journals_for_users",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "journal_id")
    )
    private Set<Journal> favoriteJournal = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<JournalRating> journalRating = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(Integer id, String username, String password, String email, boolean enabled, Set<Role> roles, Set<Book> favoriteBooks) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles;
        this.favoriteBooks = favoriteBooks;
    }

    public String encodePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public String getAllRolesNames() {
        String allNames = "";
        for(Role role: roles) {
            allNames += role.getName() + " ";
        }
        return allNames;
    }

}
