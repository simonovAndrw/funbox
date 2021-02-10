package org.testtask.funbox.controller;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.testtask.funbox.entity.Link;
import org.testtask.funbox.entity.Domain;
import org.testtask.funbox.entity.Status;
import org.testtask.funbox.repository.LinkRepository;

import java.util.Set;

@RestController
@RequestMapping("/")
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    public final LinkRepository containerRepository;

    public WebController(LinkRepository containerRepository) {
        this.containerRepository = containerRepository;
    }

    @GetMapping("/visited_domains")
    public Object getVisitedDomains(@RequestParam String from, @RequestParam String to) {

        if (Long.parseLong(from) > Long.parseLong(to))
            return Status.getErrorStatus();

        Set<String> domains = containerRepository.findAllUrlById(Long.parseLong(from), Long.parseLong(to));

        return new Domain(domains, Status.getOkStatus().getStatus());
    }

    @PostMapping("/visited_links")
    public Status addVisitedLinks(@RequestBody String json) {

        Gson gson = new Gson();
        LinkedTreeMap<?, ?> t = gson.fromJson(json, LinkedTreeMap.class);
        if (t.size() > 1)
            return Status.getErrorStatus();

        Link container = gson.fromJson(json, Link.class);

        if (container.getLinks() == null)
            return Status.getErrorStatus();

        LOGGER.info("Created container time: " + container.getId() + " vs actual time: " + System.currentTimeMillis());

        containerRepository.add(container);

        return Status.getOkStatus();
    }
}
