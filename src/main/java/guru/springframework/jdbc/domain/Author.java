package guru.springframework.jdbc.domain;

import jakarta.persistence.*;
@NamedQueries({
        @NamedQuery(name = "author_find_all", query = "SELECT a FROM Author a"),
        @NamedQuery(name = "author_find_by_name", query = "SELECT a FROM Author a WHERE a.firstName =:first_name AND a.lastName =:last_name" )
})
@NamedNativeQuery(name = "find_all_author", query = "SELECT * FROM author", resultClass = Author.class)
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
