package projectiondemo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotBlank;
import projectiondemo.domain.base.LongId;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Cepro, 2017-03-24
 */
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "readings")
public class Reading extends LongId {

    @NaturalId
    @ManyToOne(optional = false)
    private final Reader reader;

    @NaturalId
    @ManyToOne(optional = false)
    private final Book book;
    
    @NotBlank
    private String review;
    
    @Min(1) @Max(5)
    private Integer rating;
}
