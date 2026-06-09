package br.com.uel.featurestore.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration // Classe relacionada a definição de segurança, quesito criptografia de dados
public class SecurityConfig {
    @Autowired
    private SecurityFilter securityFilter;

    @Bean // Definição do tipo de criptografia adotada no projeto
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Função para manejamento do acesso de rotas, podendo aplicar filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Desabilita a proteção do CSRF, visto que sua implementação atrapalha um aplicação desenvolvida com tokens JWT
            .csrf(csrf -> csrf.disable())

            // Sessão não será guardada na memória, o controle de sessão será usado com tokens JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configuração de acesso pelas rotas
            .authorizeHttpRequests(auth -> auth
                // Verifica a paridade da requisição com a rota descrita, permitindo o acesso
                .requestMatchers(HttpMethod.POST, "/usuarios/cadastrar").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios/login").permitAll()
                .requestMatchers("/error").permitAll()
                // Qualquer outra rota posterior será acessada apenas a partir do login
                .anyRequest().authenticated()
            )
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean 
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    
        corsConfiguration.setAllowedMethods(Arrays.asList("DELETE", "GET", "PUT", "POST", "OPTIONS"));
    
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}