package com.tienda.web.app.springboot.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tienda.web.app.springboot.security.filter.JwtAuthenticationFilter;
import com.tienda.web.app.springboot.security.filter.JwtValidationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired //lo inyectamos por que es un componente de spring security
	private AuthenticationConfiguration authenticationConfiguration;
	
	//metodo
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean //genera un componente spring con una instancia de BCryptPasswordEncoder
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(requests -> requests
                .antMatchers("/h2-console/**", "/usuario", "/usuario/crear", "/marca/buscar", "/usuario/crear/admin",
                		"/role", "/role/buscar", "/role/ver/{id}", "/role/crear").permitAll()
                .antMatchers(HttpMethod.GET, "/usuario", "/usuario/ver/{id}", "/usuario/buscar").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/usuario/crear/admin").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/usuario/editar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/usuario/eliminar/{id}").hasRole("ADMIN")
                //
                .antMatchers(HttpMethod.GET, "/producto", "/producto/ver/{id}", "/producto/buscar").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/producto/crear", "/producto/crear-con-foto").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/producto/editar/{id}", "/producto/editar-con-foto/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/producto/eliminar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/producto/uploads/img/{id}").hasRole("ADMIN")
                //
                .antMatchers(HttpMethod.GET, "/carrito", "/carrito/ver/{id}").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/carrito/crear").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/carrito/editar/{id}").hasAnyRole("USER","ADMIN")
                .antMatchers(HttpMethod.DELETE, "/carrito/eliminar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/carrito/{id}/total", "/carrito/factura/{id}").hasAnyRole("USER", "ADMIN")
                //
                .antMatchers(HttpMethod.GET, "/marca", "/marca/ver/{id}").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/marca/crear", "/marca/crear-con-foto").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/marca/editar/{id}", "/marca/editar-con-foto/{id}"
                							,"/marca/{asignar-productos}", "marca/{id}/eliminar-producto").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/marca/eliminar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/marca/uploads/img/{id}").hasRole("ADMIN")
                //
                .antMatchers(HttpMethod.GET, "/itemcarrito", "/itemcarrito/ver/{id}").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/itemcarrito/crear").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/itemcarrito/editar/{id}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/itemcarrito/eliminar/{id}").hasRole("ADMIN")
                //
                .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(csrf -> {
                    try {
                        csrf.disable() //seguridad para las vitas
                                .headers(headers -> headers.frameOptions().disable());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //la sesion se va enviar en el tockent por algunos datos para que se pueda autenticar. en esta caso se deja sin estado.
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(handling -> handling.accessDeniedHandler(new CustomAccessDeniedHandler())); 
    }
	
	//Configuracion del Cors para el manejo de aplicacion como angular, aplicacion moviles Etc (Transmision de datos)
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		//creamos una instancia de corsconfiguration
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("*"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		config.setAllowCredentials(true);
		
		//instancia de de urlbaseconfiguracionsource
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);//en todas las rutas de la aplicacion se aplicara la configuracion del "Cors"
		return source;
	}
	
	//Componente para asignar corsConfigurationSource
	@Bean
	FilterRegistrationBean<CorsFilter> corsFilter(){
		FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
		corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return corsBean;
	}
}
