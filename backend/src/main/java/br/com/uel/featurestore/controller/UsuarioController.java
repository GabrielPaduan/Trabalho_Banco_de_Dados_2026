package br.com.uel.featurestore.controller;

import br.com.uel.featurestore.model.Usuario;
import br.com.uel.featurestore.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;



@RestController // Define a classe como o controlador das rotas conversando em JSON
@RequestMapping("/api/usuarios") // Define a rota para acessar as funções da classe usuário
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Nas funções abaixo usa-se o tipo de retorno ResponseEntity afim de adequar as funções para lidar de forma completa com as requisições HTTP fornecendo mensagens de retorno personalizadas facilitando o rastreamento de erros

    // Usa a biblioteca Lista para armazenarmos os usuários obtidos nessa busca
    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarUsuario() {
        try {
            List<Usuario> lista = usuarioService.pegarUsuarios();
            return ResponseEntity.status(200).body(lista);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Função que pega o usuário pelo email específico
    @GetMapping("/listar/pessoa")  
    public ResponseEntity<?> listarUsuarioPorEmail(@RequestBody Map<String, String> identificador) {
        // Usa-se o Map<String, String> para receber um JSON qualquer em formato "atributo": "valorAtributo"
        try {
            // .get("email") obtém o valor do atributo enviado para ser usado no service~dao
            String email = identificador.get("email");
            Usuario usuario = usuarioService.pegarUsuarioPorEmail(email);
            return ResponseEntity.status(200).body(usuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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

    // Função para exclusão de um usuário
    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluirUsuarioPorCpf(@RequestBody Map<String,String> identificador) {
        // Mesmo uso do Map e do get() aplicado na listagem por email
        try {
            String cpf = identificador.get("cpf");
            usuarioService.excluirUsuario(cpf);
            return ResponseEntity.status(200).body("Usuário removido com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Função para atualizar dados como email e nome do usuário
    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarUsuario(@RequestBody Usuario usuario) {
        try {
            usuarioService.atualizarUsuario(usuario);
            return ResponseEntity.status(200).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Função para realizar o login do usuário
    @PostMapping("/login")
    public ResponseEntity<String> fazerLoginUsuario(@RequestBody Map<String, String> credenciais) {
        try {
            // Separa os campos obtidos do Map em dois atributos separados
            String login = credenciais.get("email");
            String senha = credenciais.get("senhaHash");

            // Chama a função de login verificando se o login é válido ou não
            boolean valido = usuarioService.loginUsuario(login, senha);
            
            if (valido) {
                return ResponseEntity.status(200).body("Login realizado com sucesso!");
            } else {
                return ResponseEntity.status(401).body("Login ou senha incorretos!");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar o Login!");
        }
    }
    
    @PostMapping("/redefinir-senha")
    public ResponseEntity<String> redefinirSenhaUsuario(@RequestBody Map<String, String> identificador) {
        try {
            String email = identificador.get("email");
            String novaSenha = identificador.get("senhaHash");
            usuarioService.redefinirSenha(email, novaSenha);
            return ResponseEntity.status(200).body("Senha redefinida com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processor a solicitação!");
        }
        
    }
    
}