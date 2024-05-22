package view;
import service.CourseService;
import service.CourseServiceImp;

import java.util.InputMismatchException;
import java.util.Scanner;
public class View {
    private final static CourseService courseService = new CourseServiceImp();
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("=".repeat(150));
            System.out.println("1. ADD NEW COURSE");
            System.out.println("2. LIST ALL COURSES");
            System.out.println("3. FIND COURSE BY ID");
            System.out.println("4. FIND COURSE BY TITLE");
            System.out.println("5. REMOVE COURSE BY ID");
            System.out.println("0/99. EXIT");
            System.out.println("=".repeat(150));
            System.out.print("Enter your choice: ");

           try {
               int choice = scanner.nextInt();
               scanner.nextLine(); // Consume newline character

               switch (choice) {
                   case 1:
                       courseService.addNewCourse();
                       break;
                   case 2:
                       courseService.listAllCourses();
                       break;
                   case 3:
                       courseService.findCourseById();
                       break;
                   case 4:
                       courseService.findCourseByTitle();
                       break;
                   case 5:
                       courseService.removeCourseById();
                       break;
                   case 0:
                   case 99:
                       System.out.println("Exiting...");
                       return;
                   default:
                       System.out.println("Invalid choice! Please enter a number between 1 and 5.");
               }
           }catch (InputMismatchException e){
               System.out.println("Invalid input! Please enter a valid number from 1-5.");
               scanner.next(); // Clear the invalid input
           }

           }
        }
}
