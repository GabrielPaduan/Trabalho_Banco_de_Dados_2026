package br.com.uel.featurestore.controller;

import br.com.uel.featurestore.model.User;
import br.com.uel.featurestore.service.TokenService;
import br.com.uel.featurestore.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.ResponseEntity;

@RestController // Define a classe como o controlador das rotas conversando em JSON
@RequestMapping("/usuarios") // Define a rota para acessar as funções da classe usuário
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    public UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // Nas funções abaixo usa-se o tipo de retorno ResponseEntity afim de adequar as funções para lidar de forma completa com as requisições HTTP fornecendo mensagens de retorno personalizadas facilitando o rastreamento de erros

    // Usa a biblioteca Lista para armazenarmos os usuários obtidos nessa busca
    @GetMapping
    public ResponseEntity<List<User>> listUser() {
        try {
            List<User> lista = userService.getUsers();
            return ResponseEntity.status(200).body(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Função que pega o usuário pelo email específico
    @GetMapping("/{email}")  
    public ResponseEntity<?> listUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.status(200).body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping // Define uma função que ira inserir ou alterar um dado no banco de dados
    public ResponseEntity<String> createUser(@RequestBody User user) { // RequestBody faz com que o spring busque no corpo da requisição o JSON
        try { // Bloco try...catch para controle das requisições garantindo um tracking eficiente de erros
            userService.createNewUser(user); // chama o service de Usuario 
            return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!"); // Retorna status OK! em caso de sucesso
        } catch (IllegalArgumentException e) { // caso de erro de campo vazio
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) { // caso de erro interno do servidor
            return ResponseEntity.internalServerError().body("Erro no servidor interno: " + e.getMessage());
        }
    }

    // Função para exclusão de um usuário
    @DeleteMapping("/{cpf}")
    public ResponseEntity<String> excluirUsuarioPorCpf(@PathVariable String cpf) {
        try {
            userService.deleteUser(cpf);
            return ResponseEntity.status(200).body("Usuário removido com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Função para atualizar dados como email e nome do usuário
    @PutMapping
    public ResponseEntity<String> atualizarUsuario(@RequestBody User user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.status(200).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Função para realizar o login do usuário
    @PostMapping("/login")
    public ResponseEntity<String> doUserLogin(@RequestBody Map<String, String> credentials) {
        try {
            // Separa os campos obtidos do Map em dois atributos separados
            String login = credentials.get("email");
            String password = credentials.get("password");

            // Chama a função de login verificando se o login é válido ou não
            String user = userService.userLogin(login, password);

            if (user != null) {
                String token = tokenService.generateToken(login, user);
                return ResponseEntity.status(200).body(token);
            } else {
                return ResponseEntity.status(401).body("Login ou senha incorretos!");
            }
        } catch(NoSuchElementException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar o Login!");
        }
    }
}