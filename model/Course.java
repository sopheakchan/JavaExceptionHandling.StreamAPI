package model;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

public class Course {
    private int id;
    private String title;
    private String [] instructorNames;
    private String [] requirements;
    private LocalDate startDate;

    public Course(int id, String title, String[] instructorNames, String [] requirements, LocalDate startDate){
    this.id = id;
    this.title = title;
    this.instructorNames = instructorNames;
    this.requirements = requirements;
    this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getInstructorNames() {
        return instructorNames;
    }

    public void setInstructorNames(String[] instructorNames) {
        this.instructorNames = instructorNames;
    }

    public String[] getRequirements() {
        return requirements;
    }

    public void setRequirements(String[] requirements) {
        this.requirements = requirements;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", instructorNames=" + Arrays.toString(instructorNames) +
                ", requirements=" + Arrays.toString(requirements) +
                ", startDate=" + startDate +
                '}';
    }
}
