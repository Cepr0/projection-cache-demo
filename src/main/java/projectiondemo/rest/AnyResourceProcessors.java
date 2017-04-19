package projectiondemo.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.*;
import org.springframework.stereotype.Component;
import projectiondemo.domain.base.BaseEntity;
import projectiondemo.dto.Dto;
import projectiondemo.util.ProxyHelper;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Repository REST Resource processors
 *
 * @author Cepro, 2017-01-28
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AnyResourceProcessors {
    
    private final @NonNull CacheManager cacheManager;
    private final @NonNull RepositoryEntityLinks entityLinks;
    private final @NonNull RepositoryRestConfiguration restConfiguration;
    
    @Component
    public class SingleResourceProcessor implements ResourceProcessor<Resource<?>> {
        
        @Override
        public Resource<?> process(Resource<?> resource) {
            // LOG.debug("SingleResourceProcessor {}", resource.toString());
            Object content = resource.getContent();
            
            try {
                return makeDtoResource(content);
        
            } catch (Exception ignored) {
                return resource;
            }
        }
        
        /**
         * Try to instantiate a Dto from 'resource' content then make a {@link Resource} for it.
         * To instantiate a Dto this method add 'Impl' suffix to resource content interface,
         * so Dto implementation class must be in the same package as a resource content interface
         * and has name 'resource content interface name' + 'Impl'.
         *
         * @param resource a {@link Resource} object
         * @return {@link Resource} for Dto related to interface of 'resource' content
         * or input parameter if Dto implementation doesn't exists
         */
        private Resource<?> tryToGetDtoResource(Resource<?> resource) {
            Object content = resource.getContent();
            
            // TODO refactor this block - move to the util class?
            try {
                return makeDtoResource(content);
                
            } catch (Exception ignored) {
                return resource;
            }
            
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
            Collection<Resource<?>> content = resource.getContent();
            Resource<?> item = content.iterator().next();
            Object itemContent = item.getContent();
            if (isDtoImplemented(itemContent)) {
                Class baseEntity = ((Dto)itemContent).getBaseEntity();

                // TODO Find a way to make search link like "http://localhost:8080/api/books/search"
                Link link = entityLinks.linkToCollectionResource(baseEntity);
                String href = link.getHref();
                String entityPlural = href.substring(href.lastIndexOf("/"));
                String substring = href.substring(0, href.lastIndexOf("/")) + "/profile" + entityPlural;
                Link profile = new Link(substring, "profile");

                resource.add(entityLinks.linkFor(baseEntity).slash("search").withRel("search"), profile);
            }
            return resource;
        }
    }
    
    private boolean isDtoImplemented(Object content) {
        Class<?>[] interfaces = content.getClass().getInterfaces();
        return interfaces.length > 0 && interfaces[0].getInterfaces().length > 0 && interfaces[0].getInterfaces()[0].equals(Dto.class);
    }
    
    private Class<?> getDtoInterface(Object content) throws ClassNotFoundException {
        
        Class<?>[] interfaces = content.getClass().getInterfaces();
        if (interfaces.length > 0) {
            return interfaces[0];
        } else {
            throw new ClassNotFoundException();
        }
    }
    
    private Class<?> getDtoImplClass(Object content) throws ClassNotFoundException {
        
        Class<?> dtoInterface = getDtoInterface(content);
        String dtoInterfaceName = dtoInterface.getSimpleName();
        String dtoPackageName = dtoInterface.getPackage().getName();
        String dtoImplName = dtoPackageName + "." + dtoInterfaceName + "Impl";
        return Class.forName(dtoImplName);
    }
    
    private Dto getDtoImpl(Object content) throws ReflectiveOperationException {
        Class<?> dtoImplClass = getDtoImplClass(content);
        Constructor<?> dtoImplCtor = dtoImplClass.getDeclaredConstructor(getDtoInterface(content));
        return (Dto) dtoImplCtor.newInstance(content);
    }
    
    private Resource<Dto> makeDtoResource(Object content) throws Exception {
    
        if (!isDtoImplemented(content)) {
            throw new ClassNotFoundException();
        }
    
        Dto dtoImpl = getDtoImpl(content);
    
        HashMap dtoMap = (HashMap) ProxyHelper.getTargetObject(content);
        List<Link> links = new ArrayList<>();
    
        links.add(entityLinks.linkForSingleResource(dtoImpl.getBaseEntity(), dtoImpl.getId()).withSelfRel());
    
        //noinspection unchecked
        for (Map.Entry<String, Object> entry : (Set<Map.Entry<String, Object>>) dtoMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (BaseEntity.class.isAssignableFrom(value.getClass())) {
                
                // TODO Implement support for Collection resource
                // TODO Correct link like "http://localhost:8080/api/books/{id}/author"
                Link link = entityLinks.linkToSingleResource(value.getClass(), ((BaseEntity) value).getId()).withRel(key);
                
                // if (dtoImpl.getBaseEntity().equals(value.getClass()) && restConfiguration.getProjectionConfiguration().hasProjectionFor(value.getClass())) {
                //     link = new Link(link.getHref() + "{?projection}", key);
                // }
                links.add(link);
            }
        }
        return new Resource<>(dtoImpl, links);
    }
}
