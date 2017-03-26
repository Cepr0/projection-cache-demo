package projectiondemo.domain;

import projectiondemo.domain.base.LongId;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

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
    
    @ManyToOne(optional = false)
    private final Reader reader;
    
    @ManyToOne(optional = false)
    private final Book book;
    
    @NotBlank
    private String review;
    
    @Min(1) @Max(5)
    private Integer rating;
}
