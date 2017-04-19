package br.com.insideup.config;

import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.jolbox.bonecp.BoneCPDataSource;

import br.com.insideup.controller.TestRestController;
/**
 * WebAppConfiguration.java
 *
 * @author Jean Santos <jeanufu21@gmail.com>
 * @since 2017-04-18
 * @version 0.1
 */
@EnableWebMvc
@ComponentScan(basePackageClasses={TestRestController.class})
@EnableTransactionManagement
public class WebAppConfiguration extends DataBaseConfig{
	
	@Bean
	public DataSource dataSource() throws Exception{
		
		BoneCPDataSource dataSource = new BoneCPDataSource();
		dataSource.setUsername(env.getProperty("app.datasource.username"));
		dataSource.setJdbcUrl(env.getProperty("app.datasource.url"));
		dataSource.setDriverClass(env.getProperty("app.datasource.driverClass"));
		dataSource.setPassword(env.getProperty("app.datasource.password"));
		dataSource.setIdleMaxAge(240,TimeUnit.SECONDS);
		dataSource.setIdleConnectionTestPeriod(60,TimeUnit.SECONDS);
		dataSource.setPartitionCount(1);
		dataSource.setAcquireIncrement(10);
		dataSource.setMaxConnectionsPerPartition(10);
		dataSource.setMinConnectionsPerPartition(5);
		dataSource.setConnectionTimeout(20,TimeUnit.SECONDS);
		dataSource.setProperties(getHibernateProperties());
		return dataSource;	
	}
	
	@Bean(name="entityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setDataSource(dataSource);
        factoryBean.setPersistenceProvider(new HibernatePersistenceProvider());
        factoryBean.setPackagesToScan("br.com.insideup.*");
        return factoryBean;
    }
	
	@Bean(name="transactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("entityManager") EntityManagerFactory emf){
        return new JpaTransactionManager(emf);
    }
	
	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
       return new PersistenceExceptionTranslationPostProcessor();
    }
	
	
	@Bean
	public MessageSource messageSource(){
		ReloadableResourceBundleMessageSource messageSource = 
				new ReloadableResourceBundleMessageSource();
		messageSource.addBasenames("/WEB-INF/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheMillis(10);
		return messageSource;
	}
	
	@Bean
	public FormattingConversionService mvcConversionService(){
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		DateFormatterRegistrar registrar = new DateFormatterRegistrar();
		registrar.setFormatter(new DateFormatter("dd/MM/yyyy"));
		registrar.registerFormatters(conversionService);
		
		return conversionService;
	}
	
}
