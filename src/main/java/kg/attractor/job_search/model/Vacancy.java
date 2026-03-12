package kg.attractor.job_search.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vacancy {
    private int id;
    private String name;
    private int categoryId;
    private double salary;
    private int expFrom;
    private int expTo;
    private boolean isActive;
    private int authorId;
    private LocalDateTime createdDate;
    private LocalDateTime updateTime;
}
