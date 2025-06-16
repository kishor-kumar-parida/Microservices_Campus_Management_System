package com.campus;

import java.sql.*;
import java.util.Scanner;

public class Student {
    private Connection con;
    private Scanner sc;

    public Student(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void manageStudents() {
        while (true) {
            System.out.println("\n===== Student Management =====");
            displayStudents();

            System.out.print("\nDo you want to add, delete, edit or none? (add/delete/edit/none): ");
            String option = sc.nextLine().trim().toLowerCase();

            switch (option) {
                case "add":
                    addStudent();
                    break;
                case "delete":
                    deleteStudent();
                    break;
                case "edit":
                    editStudent();
                    break;
                case "none":
                    return;
                default:
                    System.out.println("❌ Invalid option! Try again.");
            }
        }
    }

    private void displayStudents() {
        String query = "SELECT * FROM student";
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("\n%-5s %-15s %-22s %-12s %-12s %-8s %-10s %-5s\n",
                    "ID", "Name", "Email", "Phone", "DOB", "Gender", "Dept", "Year");
            System.out.println("-------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-5d %-15s %-22s %-12s %-12s %-8s %-10s %-5d\n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("dob"),
                        rs.getString("gender"),
                        rs.getString("department"),
                        rs.getInt("year_of_study"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error displaying students:");
            e.printStackTrace();
        }
    }

    private void addStudent() {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine();
            System.out.print("Enter email: ");
            String email = sc.nextLine();
            System.out.print("Enter phone: ");
            String phone = sc.nextLine();
            System.out.print("Enter DOB (YYYY-MM-DD): ");
            String dob = sc.nextLine();
            System.out.print("Enter gender: ");
            String gender = sc.nextLine();
            System.out.print("Enter department: ");
            String dept = sc.nextLine();
            System.out.print("Enter year of study: ");
            int year = Integer.parseInt(sc.nextLine());

            String insert = "INSERT INTO student (name, email, phone, dob, gender, department, year_of_study) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pst = con.prepareStatement(insert)) {
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, phone);
                pst.setDate(4, Date.valueOf(dob));
                pst.setString(5, gender);
                pst.setString(6, dept);
                pst.setInt(7, year);

                int rows = pst.executeUpdate();
                System.out.println("✅ Student added! Rows affected: " + rows);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("❌ Error adding student:");
            e.printStackTrace();
        }
    }

    private void deleteStudent() {
        System.out.print("Enter student ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String delete = "DELETE FROM student WHERE student_id = ?";
        try (PreparedStatement pst = con.prepareStatement(delete)) {
            pst.setInt(1, id);
            int rows = pst.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Student deleted.");
            else
                System.out.println("⚠️ No student found with that ID.");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting student:");
            e.printStackTrace();
        }
    }

    private void editStudent() {
        System.out.print("Enter student ID to edit: ");
        int id = Integer.parseInt(sc.nextLine());

        String checkQuery = "SELECT * FROM student WHERE student_id = ?";
        try (PreparedStatement check = con.prepareStatement(checkQuery)) {
            check.setInt(1, id);
            ResultSet rs = check.executeQuery();

            if (!rs.next()) {
                System.out.println("⚠️ Student ID not found.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking student:");
            e.printStackTrace();
            return;
        }

        try {
            System.out.print("Enter new name: ");
            String name = sc.nextLine();
            System.out.print("Enter new email: ");
            String email = sc.nextLine();
            System.out.print("Enter new phone: ");
            String phone = sc.nextLine();
            System.out.print("Enter new DOB (YYYY-MM-DD): ");
            String dob = sc.nextLine();
            System.out.print("Enter new gender: ");
            String gender = sc.nextLine();
            System.out.print("Enter new department: ");
            String dept = sc.nextLine();
            System.out.print("Enter new year of study: ");
            int year = Integer.parseInt(sc.nextLine());

            String update = "UPDATE student SET name=?, email=?, phone=?, dob=?, gender=?, department=?, year_of_study=? WHERE student_id=?";
            try (PreparedStatement pst = con.prepareStatement(update)) {
                pst.setString(1, name);
                pst.setString(2, email);
                pst.setString(3, phone);
                pst.setDate(4, Date.valueOf(dob));
                pst.setString(5, gender);
                pst.setString(6, dept);
                pst.setInt(7, year);
                pst.setInt(8, id);

                int rows = pst.executeUpdate();
                System.out.println("✅ Student updated! Rows affected: " + rows);
            }
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("❌ Error updating student:");
            e.printStackTrace();
        }
    }
}
