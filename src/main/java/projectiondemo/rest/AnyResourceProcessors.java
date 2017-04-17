package projectiondemo.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.Resources;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;

/**
 * Repository REST Resource processors
 * @author Cepro, 2017-01-28
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class AnyResourceProcessors {
    
    private final @NonNull CacheManager cacheManager;
    
    @Component
    public class SingleResourceProcessor implements ResourceProcessor<Resource<?>> {
    
        @Override
        public Resource<?> process(Resource<?> resource) {
            // LOG.debug("SingleResourceProcessor {}", resource.toString());
    
            return tryToGetDtoResource(resource);
        }
    
        /**
         * Try to instantiate a DTO from 'resource' content then make a {@link Resource} for it.
         * To instantiate a DTO this method add 'Impl' suffix to resource content interface,
         * so DTO implementation class must be in the same package as a resource content interface
         * and has name 'resource content interface name' + 'Impl'.
         * @param resource a {@link Resource} object
         * @return {@link Resource} for DTO related to interface of 'resource' content
         * or input parameter if DTO implementation doesn't exists
         */
        private Resource<?> tryToGetDtoResource(Resource<?> resource) {
            Object content = resource.getContent();
            Class<?>[] interfaces = content.getClass().getInterfaces();
        
            if (interfaces.length > 0) {
                Class<?> dtoInterface = interfaces[0];
                String dtoInterfaceName = dtoInterface.getSimpleName();
                String dtoPackageName = dtoInterface.getPackage().getName();
                String dtoImplName = dtoPackageName + "." + dtoInterfaceName + "Impl";
                try {
                    Class<?> dtoImplClass = Class.forName(dtoImplName);
                    Constructor<?> dtoImplCtor = dtoImplClass.getDeclaredConstructor(dtoInterface);
                    Object dtoImpl = dtoImplCtor.newInstance(content);

                    return new Resource<>(dtoImpl);

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
            return resource;
        }
    }
}
