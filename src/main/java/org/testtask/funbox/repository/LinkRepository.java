package org.testtask.funbox.repository;

import org.testtask.funbox.entity.Link;

import java.util.Set;

public interface LinkRepository {

    void add(Link container);

    Link findById(Long time);

    Set<String> findAllUrlById(Long timeFrom, Long timeTo);
}
