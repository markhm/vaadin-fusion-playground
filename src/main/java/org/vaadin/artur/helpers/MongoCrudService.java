package org.vaadin.artur.helpers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public abstract class MongoCrudService<T, ID> {

    protected abstract MongoRepository<T, ID> getRepository();

    public Optional<T> get(ID id) {
        return getRepository().findById(id);
    }

    public T update(T entity) {
        return getRepository().save(entity);
    }

    public void delete(ID id) {
        getRepository().deleteById(id);
    }

    public Page<T> list(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public int count() {
        return (int) getRepository().count();
    }

}
