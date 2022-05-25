package co.com.invima.maestro.service.srvdocumento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
@EnableSwagger2
@EntityScan({"co.com.invima.maestro.modeloTransversal"})
public class SrvDocumentoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SrvDocumentoApplication.class, args);
    }

    /**
     * @return Docket
     * @author Jpeña Swagger Doc
     */
    @Bean
    public Docket apiDocket() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("co.com.invima.maestro.service.srvdocumento"))
                .paths(PathSelectors.any()).build().apiInfo(getApiInfo());

    }

    /**
     * @return ApiInfo
     * @author Jpeña Aplication Info
     */
    private ApiInfo getApiInfo() {
        return new ApiInfo("Documento API", "API que maneja la gestion de Documento", "1.0.0", "Rest",
                new Contact("SmartBit", "http://smartBit.com/", "smartBit@email.com"), "2019 - SmartBit",
                "http://smartBit.com/", Collections.emptyList());
    }

}
