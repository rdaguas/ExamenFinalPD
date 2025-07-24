package com.progra.distribuida.rest;

import com.progra.distribuida.db.Todo;
import com.progra.distribuida.services.TodoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@Transactional
@Path("/todos")
public class TodoRest {

    @Inject
    TodoService todoService;

    @GET
    public List<Todo> list() {
        return todoService.listTodos();
    }

    @POST
    @Path("/sync")
    public void sync() {
        todoService.syncTodosFromExternalApi();
    }
}
