package app;

import static spark.Spark.*;
import service.PessoaService;

public class Aplicacao {

    private static PessoaService pessoaService = new PessoaService();

    public static void main(String[] args) {

        staticFiles.location("/public");
        port(4567);

        get("/oi", (request, response) -> "Oi Mundo!");

        post("/pessoa", (request, response) -> pessoaService.add(request, response));

        get("/pessoa/:id", (request, response) -> pessoaService.get(request, response));

        post("/pessoa/update/:id", (request, response) -> pessoaService.update(request, response));

        get("/pessoa/delete/:id", (request, response) -> pessoaService.remove(request, response));

        get("/pessoa", (request, response) -> pessoaService.getAll(request, response));
    }
}