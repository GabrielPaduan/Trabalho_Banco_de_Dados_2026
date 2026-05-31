package br.com.uel.featurestore.controller;

import br.com.uel.featurestore.model.Usuario;
import br.com.uel.featurestore.service.UsuarioService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController // Define a classe como o controlador das rotas conversando em JSON
@RequestMapping("/api/usuario") // Define a rota para acessar as funções da classe usuário
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/cadastrar") // Define uma função que ira inserir ou alterar um dado no banco de dados
    public ResponseEntity<String> cadastrarUsuario(@RequestBody Usuario usuario) { // RequestBody faz com que o spring busque no corpo da requisição o JSON
        try { // Bloco try...catch para controle das requisições garantindo um tracking eficiente de erros
            usuarioService.cadastrarNovoUsuario(usuario); // chama o service de Usuario 
            return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!"); // Retorna status OK! em caso de sucesso
        } catch (IllegalArgumentException e) { // caso de erro de campo vazio
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) { // caso de erro interno do servidor
            return ResponseEntity.internalServerError().body("Erro no servidor interno: " + e.getMessage());
        }
    }
}