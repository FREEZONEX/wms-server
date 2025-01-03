package com.supos.app.common.config;

import com.supos.app.controller.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ResponseMessageBuilder;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import springfox.documentation.service.ResponseMessage;

import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import com.google.common.base.Predicate;
import springfox.documentation.RequestHandler;

@Configuration // 标明是配置类
@EnableSwagger2 //开启swagger功能
public class SwaggerConfig {
    Set<Class<?>> includedClasses = new HashSet<>(Arrays.asList(
            WmsWarehouseController.class,
            WmsStorageLocationController.class,
            WmsMaterialController.class,
            WmsRfidmaterialController.class,
            WmsInboundController.class,
            WmsOutboundController.class,
            WmsStocktakingController.class,
            WmsTodayController.class,
            WmsResourceController.class,
            WmsTaskController.class,
            WmsRuleController.class,
            WmsSupController.class
    ));

    List<ResponseMessage> globalResponses = new ArrayList<ResponseMessage>() {{
        add(new ResponseMessageBuilder()
                .code(HttpStatus.OK.value())
                .message(HttpStatus.OK.getReasonPhrase())
                .build());
        // Add any other response messages you want to include globally
    }};

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  // DocumentationType.SWAGGER_2 固定的，代表swagger2
//                .groupName("分布式任务系统") // 如果配置多个文档的时候，那么需要配置groupName来分组标识
                .apiInfo(apiInfo()) // 用于生成API信息
                .useDefaultResponseMessages(false) // Tells Swagger not to use default response messages
                .globalResponseMessage(RequestMethod.GET, globalResponses) // Applies globalResponses to all GET methods
                .globalResponseMessage(RequestMethod.POST, globalResponses) // Applies globalResponses to all POST methods
                .select() // select()函数返回一个ApiSelectorBuilder实例,用来控制接口被swagger做成文档
                .apis(RequestHandlerSelectors.basePackage("com.supos.app.controller")) // 用于指定扫描哪个包下的接口
                .apis(customSelector(includedClasses))
                //.paths(PathSelectors.any())// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .paths(PathSelectors.ant("/wms/**"))// 选择所有的API,如果你想只为部分API生成文档，可以配置这里
                .build();
    }

    private Predicate<RequestHandler> customSelector(Set<Class<?>> classes) {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                return classes.contains(input.declaringClass());
            }
        };
    }

    /**
     * 用于定义API主界面的信息，比如可以声明所有的API的总标题、描述、版本
     *
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("WMS Project API") //  可以用来自定义API的主标题
                .description("WMS Project Backend API Description") // 可以用来描述整体的API
                .termsOfServiceUrl("") // 用于定义服务的域名
                .version("1.0") // 可以用来定义版本。
                .build(); //
    }
}

