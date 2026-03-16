package kg.attractor.job_search.model;

import lombok.Data;

@Data
public class ContactInfo {
    private int id;
    private int typeId;
    private int resumeId;
    private String value;
}
