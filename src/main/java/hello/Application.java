package hello;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

@SpringBootApplication
public class Application implements CommandLineRunner, InitializingBean{

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

	@Override
	public void afterPropertiesSet() throws Exception {
		
		
	}
  
    /**
     * @param user
     * @throws Exception 
     */
    private void addUser(User user) throws Exception{
    	
    	if(Objects.isNull(user)){
    		throw new Exception("O objeto usuário não pode ser nulo!");
    	}
    	
    	SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate.getDataSource());
    	SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
    	insert.setTableName("usuario");
    	insert.execute(namedParameters);
    }
    
    
    private void removeUser(Long id){
    	
    	if(id > 0){
    		StringBuilder query = new StringBuilder();
        	query.append(" DELETE FROM usuario WHERE ID = ? ");
        	
        	jdbcTemplate.update(query.toString(),id);
    	}
    }
    
    private Integer count(){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT COUNT(1) FROM usuario ");
    	return jdbcTemplate.queryForObject(query.toString(),Integer.class);
    }
    
    private List<User> findAll(){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT * FROM usuario ");
    	List<User> results = jdbcTemplate.query(query.toString(), new RowMapper<User>(){
    		
    		@Override
    		public User mapRow(ResultSet rs, int r) throws SQLException {
    			User user = new User();
    			user.setId(rs.getLong("ID"));
    			user.setNome(rs.getString("NOME"));
    			user.setLogin(rs.getString("LOGIN"));
    			user.setSenha(rs.getString("SENHA"));
    			return user;
    		}
    	});
    	
    	return results;
    }
    
    private List<User> filter(String value){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT * FROM usuario WHERE nome = :nome ");
    	Map<String, String> parameters = new HashMap<>();
    	parameters.put("nome", value);
    	NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
    	List<User> results = namedParameterJdbcTemplate.query(query.toString(), parameters, new RowMapper<User>(){
    		@Override
    		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
    			User user = new User();
    			user.setId(rs.getLong("ID"));
    			user.setNome(rs.getString("NOME"));
    			user.setLogin(rs.getString("LOGIN"));
    			user.setSenha(rs.getString("SENHA"));
    			return user;
    		}
    	});
    	
    	return results;
    }
  
    @SuppressWarnings("rawtypes")
	private void printResults(List results){
    	
    	if(!results.isEmpty()){
    		IntStream.range(0, results.size()).forEach(idx -> {
    			System.out.println(String.format("%02d %s", idx,results.get(idx)));
    		});
    	}
    }

	@Override
	public void run(String... arg0) throws Exception {
		
		log.info("Criando Schema do Banco");
    	Path path = Paths.get(ClassLoader.getSystemResource("schema.sql").toURI());
    	byte[] bytes = Files.readAllBytes(path);
    	String sqlScript = new String(bytes,Charset.forName("UTF-8"));
    	jdbcTemplate.execute(sqlScript);
    	
    	
    	
    	Scanner s = new Scanner(System.in);
    	String command = "";
    	System.out.print("spring>>");
    	
    	do{
    		
    		if(!command.isEmpty()){
    			System.out.println(command);
    			
    			String[] split = command.split(" ");
    	    	if(split.length == 0){
    	    		System.err.println("ERRO: Favor inserir um commando no formato: action param1 param2 ...");
    	    	}
    	    	
    	    	String action = split[0];
    	    	List<String> params = Arrays.asList(split).stream().skip(1).collect(Collectors.toList());
    			
    	    	switch(action){
    	    	
    	    		case "add":{
    	    			User user = new User(params);
    	    			addUser(user);
    	    			System.out.printf("Usuario %s foi adicionado com sucesso!\n", user.getNome());
    	    			break;
    	    		}
    	    		case "count": {
						Integer count = count();
						System.out.printf("Total de registros: %d \n",count);
						break;
					}
					case "all": {
						List<User> all = findAll();
						printResults(all);
						break;
					}
					case "get": {
						List<User> filter = filter(params.get(0));
						printResults(filter);
						break;
					}
    	    		case "rem":{
    	    			Long id = Long.parseLong(params.get(0));
						removeUser(id);
    	    			System.out.printf("Usuario %s foi removido com sucesso!\n", id);
    	    			break;
    	    		}
    	    		default:{
    	    			s.nextLine();
    	    			s.close();
    	    			System.out.println("Encerrando...");
    	    			System.exit(0);
    	    		}
    	    	}
    			System.out.print("spring>>");
    		}
    		
    	}while(Objects.nonNull(command = s.nextLine()));
	}
}