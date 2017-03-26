package projectiondemo.domain.base;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Base class for entity with {@link Long} as a primary key.
 * @author Cepro, 2016-12-10
 */
@MappedSuperclass
@NoArgsConstructor(force = true)
@Getter
@EqualsAndHashCode(callSuper = false)
public class LongId extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;
    
    @Override
    public BaseEntity setId(Long id) {
        this.id = id;
        return this;
    }
}
