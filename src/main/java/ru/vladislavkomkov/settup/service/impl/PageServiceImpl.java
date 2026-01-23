package ru.vladislavkomkov.settup.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ru.vladislavkomkov.settup.exception.NotFoundException;
import ru.vladislavkomkov.settup.model.Page;
import ru.vladislavkomkov.settup.repository.PageRepository;
import ru.vladislavkomkov.settup.service.PageService;

@Service
public class PageServiceImpl implements PageService {
    private final PageRepository repository;
    
    public PageServiceImpl(PageRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public List<Page> getPages() {
        return repository.findAll();
    }
    
    @Override
    public Optional<Page> getPage(String url) {
        return repository.findByUrl(url);
    }
    
    @Override
    public void addPage(Page page) {
        repository.save(page);
    }
    
    @Override
    public void editPage(int id, Page newData) {
        Page existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Page not found: " + id));
        
        existing.setUrl(newData.getUrl());
        existing.setTemplateLink(newData.getTemplateLink());
        existing.setActive(newData.isActive());
        repository.save(existing);
    }
}
