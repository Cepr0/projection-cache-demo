package projectiondemo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Identifiable;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 * @author Cepro, 2017-04-17
 */
public interface Dto<T, ID extends Serializable> extends Identifiable<ID> {
    
    @SuppressWarnings("unchecked")
    @JsonIgnore
    default Class<T> getBaseEntity() {
        return  (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
