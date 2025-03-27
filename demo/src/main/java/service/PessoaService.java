package service;

import dao.PessoaDAO;
import model.Pessoa;
import spark.Request;
import spark.Response;

public class PessoaService {

    private PessoaDAO pessoaDAO;

    public PessoaService() {
        pessoaDAO = new PessoaDAO();
        pessoaDAO.conectar();
    }

    // Adiciona uma nova pessoa
    public Object add(Request request, Response response) {
        int id = Integer.parseInt(request.queryParams("id"));
        String nome = request.queryParams("nome");
        int idade = Integer.parseInt(request.queryParams("idade"));
        String cpf = request.queryParams("cpf");
        String email = request.queryParams("email");

        Pessoa pessoa = new Pessoa(id, nome, idade, cpf, email);
        pessoaDAO.inserirPessoa(pessoa);

        response.status(201);
        return "Pessoa " + id + " inserida com sucesso!";
    }

    // Obtém uma pessoa pelo ID
    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Pessoa pessoa = pessoaDAO.getPessoa(id);

        if (pessoa != null) {
            response.header("Content-Type", "application/xml");
            response.header("Content-Encoding", "UTF-8");

            return "<pessoa>\n" +
                    "\t<id>" + pessoa.getId() + "</id>\n" +
                    "\t<nome>" + pessoa.getNome() + "</nome>\n" +
                    "\t<idade>" + pessoa.getIdade() + "</idade>\n" +
                    "\t<cpf>" + pessoa.getCpf() + "</cpf>\n" +
                    "\t<email>" + pessoa.getEmail() + "</email>\n" +
                    "</pessoa>\n";
        } else {
            response.status(404);
            return "Pessoa " + id + " não encontrada.";
        }
    }

    // Atualiza uma pessoa pelo ID
    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Pessoa pessoa = pessoaDAO.getPessoa(id);

        if (pessoa != null) {
            pessoa.setNome(request.queryParams("nome"));
            pessoa.setIdade(Integer.parseInt(request.queryParams("idade")));
            pessoa.setCpf(request.queryParams("cpf"));
            pessoa.setEmail(request.queryParams("email"));

            pessoaDAO.atualizarPessoa(pessoa);

            response.status(200);
            return "Pessoa " + id + " atualizada com sucesso!";
        } else {
            response.status(404);
            return "Pessoa não encontrada.";
        }
    }

    // Remove uma pessoa pelo ID
    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        boolean status = pessoaDAO.excluirPessoa(id);

        if (status) {
            response.status(200);
            return "Pessoa " + id + " removida com sucesso!";
        } else {
            response.status(404);
            return "Pessoa não encontrada.";
        }
    }

    // Obtém todas as pessoas
    public Object getAll(Request request, Response response) {
        StringBuffer returnValue = new StringBuffer("<pessoas type=\"array\">");
        for (Pessoa pessoa : pessoaDAO.getPessoas()) {
            returnValue.append("\n<pessoa>\n" +
                    "\t<id>" + pessoa.getId() + "</id>\n" +
                    "\t<nome>" + pessoa.getNome() + "</nome>\n" +
                    "\t<idade>" + pessoa.getIdade() + "</idade>\n" +
                    "\t<cpf>" + pessoa.getCpf() + "</cpf>\n" +
                    "\t<email>" + pessoa.getEmail() + "</email>\n" +
                    "</pessoa>\n");
        }
        returnValue.append("</pessoas>");
        response.header("Content-Type", "application/xml");
        response.header("Content-Encoding", "UTF-8");
        return returnValue.toString();
    }
}
