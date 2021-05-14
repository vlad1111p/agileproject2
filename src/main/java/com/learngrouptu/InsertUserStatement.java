package com.example.learngrouptu;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertUserStatement
{
  /*  private static final String INSERT_USER = "INSERT INTO user" +
            "  (name, email, password) VALUES " +
            " (?, ?, ?);";
*/
  private static final String INSERT_USER = "INSERT INTO table_userinfo" +
          "  (Vorlesung, Kontakt, Typ, Nachricht) VALUES " +
          " (?, ?, ?, ?);";


    public static void main(String[] argv) throws SQLException {
        InsertUserStatement createTableExample = new InsertUserStatement();
        createTableExample.insertRecord();
    }

    public void insertRecord() throws SQLException {
        System.out.println(INSERT_USER);
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager
                .getConnection("jdbc:sqlite:/C:\\sqlite\\db\\LearngroupTu_DB.db"); //works without pw/user see LearngroupTuApplication  .._DB.db", "root", "root");
            // String jdbcUrl = "jdbc:sqlite:/C:\\sqlite\\db\\LearngroupTu_DB_new.db"

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            //preparedStatement.setInt(1, 1); unnötig weil AI ?
            /*
            preparedStatement.setString(1, "Tony");
            preparedStatement.setString(2, "tony@gmail.com");
            preparedStatement.setString(3, "secret");
*/
            preparedStatement.setString(1, "CoSy");
            preparedStatement.setString(2, "test@web.de");
            preparedStatement.setString(3, "2");
            preparedStatement.setString(4, "");

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

            // print SQL exception information
            printSQLException(e);
        }

        // Step 4: try-with-resource statement will auto close the connection.
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