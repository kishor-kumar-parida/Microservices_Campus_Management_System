package com.campus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static final String URL = "jdbc:mysql://localhost:3306/campus_db";
    static final String USER = "root";
    static final String PASS = "jobin123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✅ Connected to database successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Failed to connect to the database. Error:");
            e.printStackTrace();
            System.exit(1); // Stop program if DB connection fails
        }
        Scanner sc = new Scanner(System.in);

        // Object instances of each Java class
        Student student = new Student(con, sc);
        Faculty faculty = new Faculty(con, sc);
//        Course course = new Course(con, sc);
//        Enrollment enrollment = new Enrollment(con, sc);
//        Timetable timetable = new Timetable(con, sc);
//        Notification notification = new Notification(con, sc);

        while (true) {
            System.out.println("\n===== Smart Campus Management System =====");
            System.out.println("1. Manage Students");
            System.out.println("2. Manage Faculty");
            System.out.println("3. Manage Courses");
            System.out.println("4. Manage Enrollments");
            System.out.println("5. Manage Timetables");
            System.out.println("6. Manage Notifications");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = sc.nextInt();
            sc.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    student.manageStudents();
                    break;
                case 2:
                    faculty.manageFaculty();
                    break;
                case 3:
//                    course.manageCourses();
//                    break;
                case 4:
//                    enrollment.manageEnrollments();
//                    break;
                case 5:
//                    timetable.manageTimetables();
//                    break;
                case 6:
//                    notification.manageNotifications();
//                    break;
                case 7:
                    System.out.println("Exiting system... Goodbye!");
                    con.close();
                    sc.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
