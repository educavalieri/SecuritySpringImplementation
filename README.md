Sequencia para implementar Segurança :

"""Observação"""
Versão do SpringBoot 2.7.3
referência no link: https://github.com/devsuperior/spring-boot-oauth

- Incluir entidades User e Role

- Copiar as Excessões de Resources em : resources.exceptions

- copirar as Excessões de Service em: services.exceptions:

- verificar pom.xml se há a anotation do validation

    ```
    <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    ```

- Verificar pom.xml sobre o spring-cloud

    ```
    <properties>
    <java.version>17</java.version>
    <spring-cloud.version>2021.0.3</spring-cloud.version>
    </properties>

    ```

- verificar se há no pom.xml as dependencias do Oauth e do security

    ```
    <dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
    </dependency>

    <dependency>
    <groupId>org.springframework.security.oauth.boot</groupId>
    <artifactId>spring-security-oauth2-autoconfigure</artifactId>
    </dependency>
    ```

- verificar no pom.xm a existencia do dependency managment
 
    ```
    <dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
    </dependencyManagement>
    ```

- verificar no pom.xm a existencia do plugin:

  	<build>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-resources-plugin</artifactId>
			<version>3.1.0</version><!--$NO-MVN-MAN-VER$ -->
		</plugin>

		<plugin>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-maven-plugin</artifactId>
		</plugin>
	</plugins>
	</build>


Copiar as seguites classes para o package config:

- copiar a classe JwtTokenEnhancer para o package components;
- copiar as classes do package config para o mesmo package;


Inserir métodos do UserDetails na entidade User implements:

    ```

    //-----------------------------------------------------------------------
    //authentication zone:


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority()))
            .collect(Collectors.toList());
    }


    @Override
    public String getUsername() {
    return email;
    }

    @Override
    public boolean isAccountNonExpired() {
    return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
    return true;
    }

    @Override
    public boolean isEnabled() {
    return true;
    }

    public boolean hasHole(String roleName){
    for( Role role : roles){
        if(role.getAuthority().equals(roleName)){
            return true;
        }
    }
    return false;

    }
    ```

- Implementar o UserDetailsService em UserService implements

    ```
    @Service
    public class UserService implements UserDetailsService {

        @Autowired
        private AuthService authService;

        @Autowired
        private UserRepository userRepository;

        private static Logger logger = LoggerFactory.getLogger(UserService.class);

        @Transactional(readOnly = true)
        public UserDTO findById(Long id){

            authService.validateSelfOrAdmin(id);

            User entity = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            return new UserDTO(entity);

        }


        //-----------------------------------------------------------------------------------
        //authentication zone:

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            User user = userRepository.findByEmail(username);
            if (user == null){
                logger.error("user not found " + username);
                throw new UsernameNotFoundException("email not found");
            }
            logger.info("user found" + username);
            return user;
        }

    }

    ```
- 
- Incluir no UserRepository o código:

	User findByEmail(String email);

- Incluir no package services o AuthService

- alterar application.properties em resource:

    ```
    spring.profiles.active=${APP_PROFILE:test}

    spring.jpa.open-in-view=false

    security.oauth2.client.client-id=${CLIENT_ID:myclientid}
    security.oauth2.client.client-secret=${CLIENT_SECRET:myclientsecret}

    jwt.secret=${JWT_SECRET:MY-JWT-SECRET}
    jwt.duration=${JWT_DURATION:8640
    ```

- Incluir o UserDTO no package dto
