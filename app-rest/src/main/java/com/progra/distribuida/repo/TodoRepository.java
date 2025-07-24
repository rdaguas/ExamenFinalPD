package com.progra.distribuida.repo;

import com.progra.distribuida.db.Todo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class TodoRepository implements PanacheRepositoryBase<Todo, Long> {
}
