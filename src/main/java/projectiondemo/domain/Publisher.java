package projectiondemo.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import projectiondemo.domain.base.LongId;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Cepro, 2017-03-27
 */
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "publishers")
public class Publisher extends LongId {

    @NotBlank
    private final String name;

    @OneToMany(mappedBy = "publisher")
    private final List<Book> books = new ArrayList<>();
}
