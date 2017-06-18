package br.com.jeanfbs.tagfood.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jolbox.bonecp.BoneCPDataSource;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfigurations extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;
	
	@Bean
	public DataSource dataSource() throws Exception{
		
		BoneCPDataSource dataSource = new BoneCPDataSource();
		
		dataSource.setUsername(env.getProperty("app.datasource.username"));
		dataSource.setJdbcUrl(env.getProperty("app.datasource.url"));
		dataSource.setDriverClass(env.getProperty("app.datasource.driverClass"));
		
		dataSource.setPassword(env.getProperty("app.datasource.password"));
		dataSource.setPartitionCount(3);
		dataSource.setAcquireIncrement(5);
		
		dataSource.setMaxConnectionsPerPartition(30);
		dataSource.setMinConnectionsPerPartition(10);
		dataSource.setProperties(getHibernateProperties());
		
		return dataSource;
	}
	
	@Bean(name="entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws Exception {
		
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactory.setDataSource(dataSource());
        
        entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactory.setPackagesToScan("br.com.jeanfbs.tagfood.*");
        entityManagerFactory.setJpaProperties(getHibernateProperties());
        
        return entityManagerFactory;
    }
	
	@Bean(name="transactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("entityManager") EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }
	
	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
       return new PersistenceExceptionTranslationPostProcessor();
    }
	
	
	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource resource = new ReloadableResourceBundleMessageSource();
		
		resource.setBasename("classpath:messages");
		resource.setDefaultEncoding("UTF-8");
		
		return resource;
	}
	
	@Bean
	public FormattingConversionService mvcConversionService(){
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		registrar.setFormatter(new DateFormatter("dd/MM/yyyy"));
		registrar.registerFormatters(conversionService);
		
		return conversionService;
	}

	@Bean(name = "multipartResolver")
    public CommonsMultipartResolver getMultipartResolver() {
        return new CommonsMultipartResolver();
    }
	
	
	@Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
	/**
     * Configure ResourceHandlers to serve static resources like CSS/ Javascript etc...
     */
     
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
    }
	
	protected Properties getHibernateProperties(){
		Properties prop = new Properties();
		prop.setProperty("hibernate.show_sql", env.getProperty("app.hibernate.show_sql"));
//		prop.setProperty("hibernate.format_sql", env.getProperty("app.hibernate.format_sql"));
//		prop.setProperty("hibernate.generate_statistics", env.getProperty("app.hibernate.generate_statistics"));
		prop.setProperty("hibernate.dialect", env.getProperty("app.hibernate.dialect"));
		prop.setProperty("hibernate.hbm2ddl.auto", env.getProperty("app.hibernate.hbm2ddl.auto"));
		return prop;
	}
}
