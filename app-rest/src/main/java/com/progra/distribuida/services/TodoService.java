package com.progra.distribuida.services;


import com.progra.distribuida.client.UserClient;
import com.progra.distribuida.db.Todo;
import com.progra.distribuida.dto.UserDto;
import com.progra.distribuida.repo.TodoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

@ApplicationScoped
public class TodoService {

    @Inject
    TodoRepository todoRepository;

    @RestClient
    UserClient userClient;

    private static final String TODOS_URL = "https://jsonplaceholder.typicode.com/todos";

    @Transactional
    public void syncTodosFromExternalApi() {
        try {
            // Leer JSON desde la URL externa
            String json = new Scanner(new URL(TODOS_URL).openStream(), "UTF-8").useDelimiter("\\A").next();
            Jsonb jsonb = JsonbBuilder.create();
            List<Todo> todos = jsonb.fromJson(json, new java.util.ArrayList<Todo>() {}.getClass().getGenericSuperclass());

            // Enriquecer con nombre de usuario
            for (Todo todo : todos) {
                UserDto user = userClient.getUser(todo.getUserId());
                todo.setUserName(user.getName());
                todoRepository.persist(todo);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al sincronizar todos desde la API externa");
        }
    }

    public List<Todo> listTodos() {
        return todoRepository.listAll();
    }
}
