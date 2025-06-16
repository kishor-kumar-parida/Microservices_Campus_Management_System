package com.campus;

import java.sql.*;
import java.util.Scanner;

public class Faculty {
    private Connection con;
    private Scanner sc;

    public Faculty(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void manageFaculty() {
        displayFaculty();

        System.out.print("Do you want to add, delete, edit or none? (add/delete/edit/none): ");
        String action = sc.nextLine().toLowerCase();

        switch (action) {
            case "add":
                addFaculty();
                break;
            case "delete":
                deleteFaculty();
                break;
            case "edit":
                editFaculty();
                break;
            case "none":
                System.out.println("Returning to main menu.");
                return;
            default:
                System.out.println("Invalid option.");
        }

        displayFaculty();
    }

    private void displayFaculty() {
        System.out.println("\n📋 Faculty Table:");
        String query = "SELECT * FROM faculty";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.printf("%-10s %-20s %-30s %-15s %-20s\n", 
                              "faculty_id", "name", "email", "department", "designation");
            System.out.println("-------------------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-10d %-20s %-30s %-15s %-20s\n",
                        rs.getInt("faculty_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("designation"));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error displaying faculty:");
            e.printStackTrace();
        }
    }

    private void addFaculty() {
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter department: ");
        String department = sc.nextLine();
        System.out.print("Enter designation: ");
        String designation = sc.nextLine();

        String insert = "INSERT INTO faculty (name, email, department, designation) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(insert)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, department);
            pstmt.setString(4, designation);
            pstmt.executeUpdate();
            System.out.println("✅ Faculty added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding faculty:");
            e.printStackTrace();
        }
    }

    private void deleteFaculty() {
        System.out.print("Enter faculty ID to delete: ");
        int id = sc.nextInt();
        sc.nextLine();

        String delete = "DELETE FROM faculty WHERE faculty_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(delete)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Faculty deleted successfully!");
            } else {
                System.out.println("❌ Faculty ID not found.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error deleting faculty:");
            e.printStackTrace();
        }
    }

    private void editFaculty() {
        System.out.print("Enter faculty ID to edit: ");
        int id = sc.nextInt();
        sc.nextLine();

        String check = "SELECT * FROM faculty WHERE faculty_id = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(check)) {
            checkStmt.setInt(1, id);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("❌ Faculty ID not found.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("❌ Error checking faculty ID:");
            e.printStackTrace();
            return;
        }

        System.out.print("Enter new name: ");
        String name = sc.nextLine();
        System.out.print("Enter new email: ");
        String email = sc.nextLine();
        System.out.print("Enter new department: ");
        String department = sc.nextLine();
        System.out.print("Enter new designation: ");
        String designation = sc.nextLine();

        String update = "UPDATE faculty SET name = ?, email = ?, department = ?, designation = ? WHERE faculty_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(update)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, department);
            pstmt.setString(4, designation);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("✅ Faculty updated successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error updating faculty:");
            e.printStackTrace();
        }
    }
}
