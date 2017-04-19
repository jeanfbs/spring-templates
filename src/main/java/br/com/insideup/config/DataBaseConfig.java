package br.com.insideup.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * DataBaseConfig.java
 *
 * @author Jean Santos <jeanufu21@gmail.com>
 * @since 2017-04-18
 * @version 0.1
 */
@Configuration
@PropertySource(value={"classpath:application.properties"})
public abstract class DataBaseConfig {

	@Autowired
	protected Environment env;
	
	
	protected Properties getHibernateProperties(){
		Properties prop = new Properties();
		prop.setProperty("app.hibernate.show_sql", env.getProperty("app.hibernate.show_sql"));
		prop.setProperty("app.hibernate.format_sql", env.getProperty("app.hibernate.format_sql"));
		prop.setProperty("app.hibernate.generate_statistics", env.getProperty("app.hibernate.generate_statistics"));
		prop.setProperty("app.hibernate.dialect", env.getProperty("app.hibernate.dialect"));
		prop.setProperty("app.hibernate.hbm2ddl.auto", env.getProperty("app.hibernate.hbm2ddl.auto"));
		return prop;
	}
	
}
