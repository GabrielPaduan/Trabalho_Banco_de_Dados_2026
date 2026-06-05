package br.com.uel.featurestore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Classe relacionada a definição de segurança, quesito criptografia de dados
public class ConfigSeguranca {
    @Bean // Definição do tipo de criptografia adotada no projeto
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // Função para manejamento do acesso de rotas, podendo aplicar filtros
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita a proteção do CSRF, visto que sua implementação atrapalha um aplicação desenvolvida com tokens JWT
            .csrf(csrf -> csrf.disable())

            // Sessão não será guardada na memória, o controle de sessão será usado com tokens JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Configuração de acesso pelas rotas
            .authorizeHttpRequests(auth -> auth
                // Verifica a paridade da requisição com a rota descrita, permitindo o acesso
                // .requestMatchers(HttpMethod.POST, "/api/usuario/cadastrar").permitAll()
                
                // Qualquer outra rota posterior será acessada apenas a partir do login
                .anyRequest().permitAll()
            );

        return http.build();
    }
}