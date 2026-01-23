package ru.vladislavkomkov.settup.model;

public class Static {
    private String name;
    private String ext;
    private String path;
    private String content;
    
    public Static() {}
    
    public Static(String name, String path, String content) {
        this.name = name;
        this.content = content;
        this.path = path;
        int dotIndex = name.lastIndexOf('.');
        this.ext = dotIndex > 0 ? name.substring(dotIndex + 1) : "";
    }
    
    public Static(String path, String name, String ext, String content) {
        this.path = path;
        this.name = name;
        this.ext = ext;
        this.content = content;
    }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getExt() { return ext; }
    public void setExt(String ext) { this.ext = ext; }
    
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}