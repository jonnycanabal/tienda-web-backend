package com.tienda.web.app.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tienda.web.app.models.entity.User;
import com.tienda.web.app.models.repository.UserRepository;

//Clase para implementar nuestro "Login"
@Service
public class JpaUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<User> userOptional = repository.findByUsername(username);
		
		//Si el usuario esta vacio, lanza este mensaje 
		if(!userOptional.isPresent()) {
			throw new UsernameNotFoundException(String.format("username %s no existe en el sistema", username));
		}
		
		User user = userOptional.orElseThrow(()-> new UsernameNotFoundException("username %s no existe en el sistema"));
		
		
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toList());
		
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				user.isEnabled(),
				true,//accountNonExpired
				true,//credentialsNonExpired
				true,//accountNonLocked
				authorities);
	}

}
