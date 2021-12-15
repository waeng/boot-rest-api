package waeng.bootrestapi.config.swagger;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author waeng
 * @since 2021/12/15
 */
public class SwaggerConfig {

    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    @Bean
    public Docket api(@Value("${swagger.title}") String title,
                      @Value("${swagger.base-package}") String basePackage) {

        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder().title(title).build())
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build()
                .extensions(openApiExtensionResolver.buildExtensions("default"));
    }

}
