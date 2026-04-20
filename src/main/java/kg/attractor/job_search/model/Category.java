package kg.attractor.job_search.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @OneToMany(mappedBy = "category")
    private List<Resume> resumes;

    @OneToMany(mappedBy = "category")
    private List<Vacancy> vacancies;

    public Integer getParentId() {
        return parent != null ? parent.getId() : null;
    }

    public void setParentId(Integer parentId) {
        if (parentId == null) {
            this.parent = null;
            return;
        }

        Category category = new Category();
        category.setId(parentId);
        this.parent = category;
    }
}