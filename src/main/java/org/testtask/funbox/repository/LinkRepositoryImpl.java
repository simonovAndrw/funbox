package org.testtask.funbox.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.testtask.funbox.entity.Link;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class LinkRepositoryImpl implements LinkRepository {

    private static final String KEY = "LINK";
    private RedisTemplate<String, Link> redisTemplate;
    private HashOperations hashOperations;

    @Autowired
    public LinkRepositoryImpl(RedisTemplate<String, Link> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void add(final Link container) {
        String[] links = Arrays.stream(container.getLinks())
                                    .distinct()
                                    .collect(Collectors.toList())
                                    .toArray(new String[0]);

        hashOperations.put(KEY, container.getId(), links);
    }

    @Override
    public Link findById(Long time) {
        return (Link) hashOperations.get(KEY, time);
    }

    @Override
    public Set<String> findAllUrlById(final Long timeFrom, final Long timeTo) {

        Map<Long, String[]> entries = hashOperations.entries(KEY);

        return entries.entrySet()
                .stream()
                .filter(x -> x.getKey() >= timeFrom && x.getKey() <= timeTo)
                .map(Map.Entry::getValue)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }
}
