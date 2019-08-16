package ${package}.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ${package}.interceptors.MDCInterceptor;
import ${package}.mappers.HolaMundoMapper;

@Configuration
@SpringBootApplication
@ComponentScan(basePackages = { "${package}.controllers", "${package}.services",
	"${package}.controllers_advices", "${package}.repo" })
public class GlobalConfiguration implements WebMvcConfigurer {

    @Bean
    public HandlerInterceptor addMDCInterceptor() {
	return new MDCInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	registry.addInterceptor(addMDCInterceptor()).addPathPatterns("/**");
    }

    /**
     * obtiene la configuración de ambiente
     */
    @Autowired
    private Environment env;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
	registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
	registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /*
     * Si desea tener otra conexion a base de datos debe repetir esta configuracion
     * en un nuevo beans dataSource() transactionManager() sqlSessionFactory()
     * mapperFactoryNomina() -- este ultimo es uno por archivo xml si desea crear
     * nuevos archivos de mapeo debe crear un nuevo bean de mapperFactory
     */

    /**
     * pool de conexiones
     * 
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource() {
	HikariConfig config = new HikariConfig();
	config.setPoolName("HickariPool - api-ejemplo");
	config.setDriverClassName(env.getProperty("${package}.driver-class-name"));
	config.setJdbcUrl(env.getProperty("${package}.datasource.url"));
	config.setUsername(env.getProperty("${package}.datasource.username"));
	config.setPassword(env.getProperty("${package}.datasource.password"));
	config.addDataSourceProperty("cachePrepStmts", "true");
	config.addDataSourceProperty("prepStmtCacheSize", "250");
	config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
	config.setMinimumIdle(1);
	config.setMaximumPoolSize(
		Integer.parseInt(env.getProperty("${package}.datasource.pool-size").trim()));
	return new HikariDataSource(config);
    }

    /**
     * administrador de transacciones
     * 
     * @return
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager() {
	return new DataSourceTransactionManager(dataSource());
    }

    /**
     * Sql factory que inicializa el map de BD
     * 
     * @return
     * @throws Exception
     */
    @Bean(name = "sqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory() throws Exception {
	SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
	sessionFactory.setDataSource(dataSource());
	sessionFactory.setTypeHandlersPackage("${package}.typehandlers");
	Resource[] arrResource = new PathMatchingResourcePatternResolver().getResources("mappers/postgresql/*.xml");
	sessionFactory.setMapperLocations(arrResource);
	return sessionFactory.getObject();
    }

    /**
     * Bindinfg de interfaz con archivo de mapeo
     * 
     * @return
     * @throws Exception
     */
    @Bean(name = "mapperHolaMundo")
    public MapperFactoryBean<HolaMundoMapper> mapperFactoryNomina() throws Exception {
	MapperFactoryBean<HolaMundoMapper> mapperFactory = new MapperFactoryBean<HolaMundoMapper>();
	mapperFactory.setMapperInterface(cl.trabajando.api_ejemplo.mappers.HolaMundoMapper.class);
	mapperFactory.setSqlSessionFactory(sqlSessionFactory());
	return mapperFactory;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
	ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
	bundle.setDefaultEncoding("UTF-8");
	Properties fileEncodings = new Properties();
	fileEncodings.setProperty("application-messages", "UTF-8");
	bundle.setFileEncodings(fileEncodings);
	bundle.setFallbackToSystemLocale(true);
	bundle.setBasename("classpath:application-messages");
	return bundle;
    }
}
