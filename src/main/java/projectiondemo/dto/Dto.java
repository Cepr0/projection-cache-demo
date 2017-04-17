package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Identifiable;

import java.io.Serializable;

/**
 * @author Cepro, 2017-04-17
 */
public interface Dto<T, ID extends Serializable> extends Identifiable<ID> {
    
    @JsonIgnore
    Class<T> getBaseEntity();
}
