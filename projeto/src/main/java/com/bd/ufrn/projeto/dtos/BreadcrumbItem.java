package com.bd.ufrn.projeto.dtos;

public class BreadcrumbItem {
    private String title;
    private String url;
    private boolean active;

    public BreadcrumbItem(String title, String url, boolean active) {
        this.title = title;
        this.url = url;
        this.active = active;
    }

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
