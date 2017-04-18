package projectiondemo.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.*;
import org.springframework.hateoas.core.Relation;
import org.springframework.stereotype.Component;
import projectiondemo.domain.base.BaseEntity;
import projectiondemo.dto.Dto;
import projectiondemo.util.ProxyHelper;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Repository REST Resource processors
 * @author Cepro, 2017-01-28
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AnyResourceProcessors {
    
    private final @NonNull CacheManager cacheManager;
    private final @NonNull EntityLinks entityLinks;
    private final @NonNull RepositoryRestConfiguration restConfiguration;
    
    @Component
    public class SingleResourceProcessor implements ResourceProcessor<Resource<?>> {
    
        @Override
        public Resource<?> process(Resource<?> resource) {
            // LOG.debug("SingleResourceProcessor {}", resource.toString());
    
            return tryToGetDtoResource(resource);
//            return resource;

        }
    
        /**
         * Try to instantiate a Dto from 'resource' content then make a {@link Resource} for it.
         * To instantiate a Dto this method add 'Impl' suffix to resource content interface,
         * so Dto implementation class must be in the same package as a resource content interface
         * and has name 'resource content interface name' + 'Impl'.
         * @param resource a {@link Resource} object
         * @return {@link Resource} for Dto related to interface of 'resource' content
         * or input parameter if Dto implementation doesn't exists
         */
        private Resource<?> tryToGetDtoResource(Resource<?> resource) {
            Object content = resource.getContent();
            Class<?>[] interfaces = content.getClass().getInterfaces();
        
            // TODO refactor this block - move to the util class?
            if (interfaces.length > 0 && interfaces[0].getInterfaces().length > 0 && interfaces[0].getInterfaces()[0].equals(Dto.class)) {
                Class<?> dtoInterface = interfaces[0];
                String dtoInterfaceName = dtoInterface.getSimpleName();
                String dtoPackageName = dtoInterface.getPackage().getName();
                String dtoImplName = dtoPackageName + "." + dtoInterfaceName + "Impl";
                try {
                    Class<?> dtoImplClass = Class.forName(dtoImplName);
                    Constructor<?> dtoImplCtor = dtoImplClass.getDeclaredConstructor(dtoInterface);
                    Dto dtoImpl = (Dto) dtoImplCtor.newInstance(content);
    
                    HashMap dtoMap = (HashMap) ProxyHelper.getTargetObject(content);
                    List<Link> links = new ArrayList<>();
    
                    //noinspection unchecked
                    for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) dtoMap.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        if (BaseEntity.class.isAssignableFrom(value.getClass())) {
    
                            // TODO Implement support for Collection resource
                            Link link = entityLinks.linkForSingleResource(value.getClass(), ((BaseEntity) value).getId()).withRel(key);

                            if (dtoImpl.getBaseEntity().equals(value.getClass()) && restConfiguration.getProjectionConfiguration().hasProjectionFor(value.getClass())) {
                                link = new Link(link.getHref() + "{?projection}", key);
                            }
                            links.add(link);
                        }
                    }
                    if (dtoImplClass.isAnnotationPresent(Relation.class)) {
                        Relation relation = dtoImplClass.getAnnotation(Relation.class);
                        links.add(entityLinks.linkForSingleResource(dtoImpl.getBaseEntity(), dtoImpl.getId()).withSelfRel());
                    }
                    return new Resource<>(dtoImpl, links);

                } catch (Exception ignored) {
                }
            }
            return resource;
        }
    }
    
    @Component
    public class MultiResourceProcessor implements ResourceProcessor<Resources<Resource<?>>> {
    
        @Override
        public Resources<Resource<?>> process(Resources<Resource<?>> resource) {
            // LOG.debug("MultiResourceProcessor {}", resource.toString());
            return resource;
        }
    }
    
    @Component
    public class PagedResourceProcessor implements ResourceProcessor<PagedResources<Resource<?>>> {
    
        @Override
        public PagedResources<Resource<?>> process(PagedResources<Resource<?>> resource) {
            // LOG.debug("PagedResourceProcessor {}", resource.toString());
            
            // TODO Implement links for page: profile and search
            return resource;
        }
    }
}
