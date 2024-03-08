package sut.edu.zyp.dormitory.manage.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.persistence.Entity;
import java.util.Set;

/**
 * 登录配置
 *
 * @author zyp
 * @version 0.0.1
 * @since 0.0.1
 */
@Configuration
public class LoginConfig implements WebMvcConfigurer {

    @Bean
    //@Bean 注解通常与 @Configuration 注解一起使用，表示这是一个配置类，并且 Spring 应该在该类中查找 bean 的定义。除了 @Bean 注解，还有其他一些注解用于更细粒度的配置，例如 @Component, @Service, @Repository, 和 @Controller 等。这些注解的使用方式与 @Bean 类似，但是它们通常用于标识不同层次或用途的组件。
    //使用 @Bean 注解的方法通常包含了创建、配置和初始化 bean 实例的逻辑。这个对象将被纳入 Spring 容器进行管理。在运行时，Spring 将会调用 myBean 方法并将其返回的对象注册为一个 bean。

    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
                // 创建一个ClassPathScanningCandidateComponentProvider对象，用于扫描实体类
                final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
                // 添加实体类的注解过滤器
                provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
                // 获取实体类的BeanDefinition
                final Set<BeanDefinition> beans = provider.findCandidateComponents("sut.edu.zyp.dormitory.manage.entity");
                // 遍历实体类的BeanDefinition，并 expose their `id` fields
                for (final BeanDefinition bean : beans) {
                    try {
                        // 获取实体类的类名
                        config.exposeIdsFor(Class.forName(bean.getBeanClassName()));
                    } catch (final ClassNotFoundException e) {
                        throw new IllegalStateException("Failed to expose `id` field due to", e);
                    }
                }
            }
        };
    }

   @Bean//MappedInterceptor 是Spring框架提供的一个通用拦截器，用于映射特定路径的请求。
    public MappedInterceptor dataRestMappedInterceptor() {
        // 创建一个MappedInterceptor对象，拦截所有/**开头的请求，
       // 排除/captcha、/400.html、/500.html、/**/*.js、/**/*.css、/**/*.woff、
       // /**/*.woff2、/**/*.ttf、/**/*.eot、/**/*.otf、/**/*.svg、/**/*.less、
       // /**/*.scss、/**/*.jpg、/**/*.ico、/**/*.jpeg、/**/*.png、/**/*.bmp请求，
       // 并使用LoginInterceptor进行拦截
        return new MappedInterceptor(new String[]{"/**"}, new String[]{
                "/login",
                "/login.html",
                "/captcha",
                "/400.html",
                "/500.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.ttf",
                "/**/*.eot",
                "/**/*.otf",
                "/**/*.svg",
                "/**/*.less",
                "/**/*.scss",
                "/**/*.jpg",
                "/**/*.ico",
                "/**/*.jpeg",
                "/**/*.png",
                "/**/*.bmp"}, new LoginInterceptor());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册LoginInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginInterceptor());
        // 所有路径都被拦截
        registration.addPathPatterns("/**");
        // 添加不拦截路径
        registration.excludePathPatterns(
                "/login",
                "/captcha",
                "/login.html",
                "/400.html",
                "/500.html",
                "/**/*.js",
                "/**/*.css",
                "/**/*.woff",
                "/**/*.woff2",
                "/**/*.ttf",
                "/**/*.eot",
                "/**/*.otf",
                "/**/*.svg",
                "/**/*.less",
                "/**/*.scss",
                "/**/*.jpg",
                "/**/*.ico",
                "/**/*.jpeg",
                "/**/*.png",
                "/**/*.bmp"
        );
    }
}