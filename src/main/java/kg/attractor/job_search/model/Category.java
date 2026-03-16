package kg.attractor.job_search.model;

import lombok.Data;

@Data
public class Category {
    private int id;
    private String name;
    private int parentId;
}
