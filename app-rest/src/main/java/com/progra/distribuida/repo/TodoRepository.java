package com.progra.distribuida.repo;

import com.progra.distribuida.db.Todo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class TodoRepository implements PanacheRepository<Todo> {
}
