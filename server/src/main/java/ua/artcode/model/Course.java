package ua.artcode.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;


/**
 * Created by v21k on 15.04.17.
 */
@Entity
@Table(name = "COURSES")
public class Course {
    @Id
    @GeneratedValue
    @Column(name = "COURSE_ID")
    private Integer id;
    private String name;
    private String author;
    @Pattern(regexp = "^http(s?):.+\\.git$", message = "Invalid git URL")
    private String url;
    private String localPath;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Lesson> lessons;
    private String description;

    @NotNull
    private String sourcesRoot;
    @NotNull
    private String testsRoot;

    @Column(length = 5000)
    private String[] dependencies;

    public Course() {
    }

    @Deprecated
    public Course(int id, String name, String author, String url, String localPath, List<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.url = url;
        this.localPath = localPath;
        this.lessons = lessons;
    }

    @Deprecated
    public Course(String name, String author, String url, String localPath, List<Lesson> lessons) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.localPath = localPath;
        this.lessons = lessons;
    }

    public Course(String name, String author, String url) {
        this.name = name;
        this.author = author;
        this.url = url;
    }

    public Course(String name, String author, String url, String description, String sourcesRoot, String testsRoot) {
        this.name = name;
        this.author = author;
        this.url = url;
        this.description = description;
        this.sourcesRoot = sourcesRoot;
        this.testsRoot = testsRoot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }

    public String getSourcesRoot() {
        return sourcesRoot;
    }

    public void setSourcesRoot(String sourcesRoot) {
        this.sourcesRoot = sourcesRoot;
    }

    public String getTestsRoot() {
        return testsRoot;
    }

    public void setTestsRoot(String testsRoot) {
        this.testsRoot = testsRoot;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (name != null ? !name.equals(course.name) : course.name != null) return false;
        if (author != null ? !author.equals(course.author) : course.author != null) return false;
        return url != null ? url.equals(course.url) : course.url == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", url='" + url + '\'' +
                ", localPath='" + localPath + '\'' +
                ", lessons=" + lessons +
                '}';
    }
}

