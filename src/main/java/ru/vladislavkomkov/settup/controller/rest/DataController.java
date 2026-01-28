package ru.vladislavkomkov.settup.controller.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.model.data.DataTopic;
import ru.vladislavkomkov.settup.model.query.QueryRequest;
import ru.vladislavkomkov.settup.service.DataService;
import ru.vladislavkomkov.settup.service.PageService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class DataController {
    private final DataService dataService;
    private final PageService pageService;

    public DataController(DataService dataService, PageService pageService) {
        this.dataService = dataService;
        this.pageService = pageService;
    }

    @PostMapping("/query")
    public ResponseEntity<List<Map<String, Object>>> executeQuery(@RequestBody QueryRequest request) {
        List<Map<String, Object>> result = dataService.executeTypedQuery(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pages")
    public ResponseEntity<List<Page>> pages() {
        List<Page> pages = pageService.getPages();
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/topics")
    public ResponseEntity<List<DataTopic>> topics() {
        List<DataTopic> topics = dataService.getTopics();
        return ResponseEntity.ok(topics);
    }
}
