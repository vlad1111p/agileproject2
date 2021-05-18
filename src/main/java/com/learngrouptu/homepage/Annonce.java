package com.learngrouptu.homepage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@RestController
public class Annonce {

    private static final String INSERT_USER = "INSERT INTO annonce" +
            "  (Vorlesung, Kontakt, Typ, Nachricht) VALUES " +
            " (?, ?, ?, ?);";

    @GetMapping("/annonce_created")
    public String SetAnnonce (@RequestParam(value = "vorlName", required = true) String vorlName,
                         @RequestParam(value = "choice", required = true) String choice,
                         @RequestParam(value = "kontakt", required = true) String kontakt,
                         @RequestParam(value = "Nachricht", required = true) String nachricht) {
        System.out.println(INSERT_USER);
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager
                .getConnection("jdbc:sqlite:./LearngroupTU_DB.db"); //works without pw/user see LearngroupTuApplication  .._DB.db", "root", "root");

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, vorlName);
            preparedStatement.setString(2, kontakt);
            if (choice.equals("Lerngruppe")) preparedStatement.setString(3, "Lerngruppe");
            if (choice.equals("Übung")) preparedStatement.setString(3, "Uebungsgruppe");
            if (!nachricht.isEmpty()) preparedStatement.setString(4, nachricht);

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }

        // Step 4: try-with-resource statement will auto close the connection.
        return vorlName +" "+choice+ " " +kontakt+ " " +nachricht;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
