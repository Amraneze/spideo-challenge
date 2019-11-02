package tv.spideo.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.zalando.problem.ProblemModule;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

@Configuration
public class JacksonConfiguration {

    @Bean
    @Primary
    public ObjectMapper serializingObjectMapper() {
        ObjectMapper mapper = new ObjectMapper()
                //To not have all the stack if an exception occured
                .registerModule(new ProblemModule())
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                //Support Java instant serialization/deserialization
                .registerModule(new JavaTimeModule());
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            // borrowed from: http://jackson-users.ning.com/forum/topics/how-to-not-include-type-info-during-serialization-with
            @Override
            protected TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> config, Annotated ann, JavaType baseType) {
                // Don't serialize JsonTypeInfo Property includes
                if (ann.hasAnnotation(JsonTypeInfo.class)
                        && ann.getAnnotation(JsonTypeInfo.class).include() == JsonTypeInfo.As.PROPERTY
                        && SerializationConfig.class.isAssignableFrom(config.getClass())) {
                    return null;
                }
                return super._findTypeResolver(config, ann, baseType);
            }
        });
        return mapper;
    }

}
