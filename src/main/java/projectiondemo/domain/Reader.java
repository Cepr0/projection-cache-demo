package projectiondemo.domain;

import projectiondemo.domain.base.LongId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cepro, 2017-03-24
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "readers")
public class Reader extends LongId {
    
    @NotBlank
    private final String name;
    
    @NotBlank
    @Email
    @Column(unique = true)
    private final String email;
    
    @ManyToMany
    @JoinTable(name = "readings", joinColumns = @JoinColumn(name = "reader_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    private final List<Book> books = new ArrayList<>();
}
