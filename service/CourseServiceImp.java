package service;

import exception.ExcessiveSpaceException;
import exception.MissingInputException;
import model.Course;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.Table;
import java.io.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


public class CourseServiceImp implements CourseService{
    private static final String MAIN_FILE = "src\\repository\\course.dat";
    @Override
    public void addNewCourse() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int id = 1000 + random.nextInt(9000);

        try {
            System.out.print("Enter Course Title : ");
            String title = scanner.nextLine();
            if (title.isEmpty()) {
                throw new MissingInputException("Course title cannot be empty.");
            }

            System.out.print("Enter Instructor Names ([,] for separated): ");
            String[] instructorNames = scanner.nextLine().split(",");
            if (instructorNames.length == 1 && instructorNames[0].isEmpty()) {
                throw new MissingInputException("At least one instructor name is required.");
            }

            System.out.print("Enter Requirements ([,] for separated): ");
            String[] requirements = scanner.nextLine().split(",");
            if (requirements.length == 1 && requirements[0].isEmpty()) {
                throw new MissingInputException("At least one requirement is required.");
            }

            // generate local date
            LocalDate startDate = LocalDate.now();

            Course course = new Course(id, title, instructorNames, requirements, startDate);
            courses.add(course);

            String data = course.getId() + "," +
                    course.getTitle() + "," +
                    Arrays.toString(course.getInstructorNames()) + "," +
                    Arrays.toString(course.getRequirements()) + "," +
                    course.getStartDate().toString();

            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(MAIN_FILE, true))) {
                bufferedWriter.write(data);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                System.out.println("[!] Problem during adding new course: " + e.getMessage());
            }
            System.out.println("Course added successfully!");
        } catch (MissingInputException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void listAllCourses() {
        loadCoursesFromFile();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
            table.addCell("ID");
            table.addCell("COURSE(S)");
            table.addCell("INSTRUCTOR(S)");
            table.addCell("REQUIREMENT(S)");
            table.addCell("STARTED DATE");

            for (Course course : courses) {
                table.addCell(String.valueOf(course.getId()));
                table.addCell(course.getTitle());
                table.addCell(String.join(", ", course.getInstructorNames()));
                table.addCell(String.join(", ", course.getRequirements()));
                table.addCell(course.getStartDate().toString());
            }
            System.out.println(table.render());
        }
    }

    @Override
    public void findCourseById() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Course ID: ");
            String input = scanner.nextLine();
            checkForExcessiveSpaces(input);

            int id = Integer.parseInt(input.trim());
//            int id = scanner.nextInt();
//            scanner.nextLine(); // Consume newline

            Course course = courses.stream()
                    .filter(c -> c.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (course != null) {
                Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER_WIDE, ShownBorders.ALL);
                table.addCell("ID");
                table.addCell("COURSE(S)");
                table.addCell("INSTRUCTOR(S)");
                table.addCell("REQUIREMENT(S)");
                table.addCell("STARTED DATE");

                table.addCell(String.valueOf(course.getId()));
                table.addCell(course.getTitle());
                table.addCell(String.join(", ", course.getInstructorNames()));
                table.addCell(String.join(", ", course.getRequirements()));
                table.addCell(course.getStartDate().toString());

                System.out.println(table.render());

            } else {
                System.out.println("Course is not found.");
            }
        } catch (ExcessiveSpaceException e){
            System.out.println("Erorr : " + e.getMessage());
        }
        catch (Exception e) {
            System.out.println("Error finding course by ID: " + e.getMessage());
        }
    }

    @Override
    public void findCourseByTitle() {
        Scanner scanner = new Scanner(System.in);
        try {
        System.out.print("Enter Course Title: ");
        String title = scanner.nextLine();
        checkForExcessiveSpaces(title);

        List<Course> foundCourses = courses.stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(title))
                .toList();


        if (foundCourses.isEmpty()) {
            System.out.println("No courses found with the given title.");
        } else {
            foundCourses.forEach(System.out::println);
        }
        }catch (ExcessiveSpaceException e) {
            System.out.println("Error: " + e.getMessage());

        }
    }

    @Override
    public void removeCourseById() {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter Course ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            boolean removed = courses.removeIf(c -> c.getId() == id);

            if (removed) {
                writeAllCoursesToFile();
                System.out.println("Course removed successfully!");
            } else {
                System.out.println("Course not found.");
            }
        } catch (Exception e) {
            System.out.println("Error removing course by ID: " + e.getMessage());
        }
    }

    @Override
    public void loadCoursesFromFile() {
        courses.clear(); // Clear the list to avoid duplicates

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(MAIN_FILE))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String[] instructorNames = parts[2].split(";");
                    String[] requirements = parts[3].split(";");
                    LocalDate startDate = LocalDate.parse(parts[4]);

                    Course course = new Course(id, title, instructorNames, requirements, startDate);
                    courses.add(course);
                }
            }
        } catch (IOException e) {
            System.out.println("[!] Problem during loading courses: " + e.getMessage());
        }
    }

    @Override
    public void writeAllCoursesToFile() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(MAIN_FILE))) {
            for (Course course : courses) {
                String data = course.getId() + "," +
                        course.getTitle() + "," +
                        String.join(";", course.getInstructorNames()) + "," +
                        String.join(";", course.getRequirements()) + "," +
                        course.getStartDate();
                bufferedWriter.write(data);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("[!] Problem during writing courses to file: " + e.getMessage());
        }
    }
    private void checkForExcessiveSpaces(String input) throws ExcessiveSpaceException {
        if (input.length() - input.trim().length() > 2) {
            throw new ExcessiveSpaceException("Input contains excessive spaces.");
        }
    }

}

