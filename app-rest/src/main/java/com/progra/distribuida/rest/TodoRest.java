package com.progra.distribuida.rest;

import com.progra.distribuida.client.UserRestClient;
import com.progra.distribuida.db.Todo;
import com.progra.distribuida.dto.TodoDto;
import com.progra.distribuida.repo.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.modelmapper.ModelMapper;

@ApplicationScoped
@Path("/todos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoRest {

    @Inject
    @ConfigProperty(name = "quarkus.http.port")
    Integer appPort;

    @Inject
    ModelMapper mapper;

    @Inject
    TodoRepository todoRepository;

    @Inject
    @RestClient
    UserRestClient userRestClient;

   @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        var todo = todoRepository.findById(id);
        if (todo == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Llama al servicio externo para obtener el nombre del usuario
        var user = userRestClient.findAll((long) Math.toIntExact(todo.getUserId()));
        TodoDto dto = new TodoDto();
        dto.setId(todo.getId());
        dto.setUserId(todo.getUserId());
        dto.setTitle(todo.getTitle());
        dto.setCompleted(todo.isCompleted());
        dto.setNombre(user.getName());
        return Response.ok(dto).build();
    }

    @GET
    @Path("/externos")
    public Response getTodosExternos() {
        try {
            java.net.URL url = new java.net.URL("https://jsonplaceholder.typicode.com/todos");
            java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            int status = con.getResponseCode();
            if (status != 200) {
                return Response.status(status).build();
            }
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            // Devuelve el string como JSON
            return Response.ok(content.toString()).type(MediaType.APPLICATION_JSON).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
