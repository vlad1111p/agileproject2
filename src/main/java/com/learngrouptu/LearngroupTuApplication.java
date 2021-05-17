package com.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;


import java.sql.*;
//inport java.SQLException;

@SpringBootApplication
@RestController
public class LearngroupTuApplication {

    public static void main(String[] args) {
        String typString = null;
        SpringApplication.run(LearngroupTuApplication.class, args);


        String jdbcUrl = "jdbc:sqlite:./LearngroupTU_DB.db";
        try{


            Connection connection = DriverManager.getConnection(jdbcUrl);


            String sql ="SELECT * FROM annonce";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                Integer id = result.getInt("aID");
                String name = result.getString("Vorlesung");
                String kontakt = result.getString("Kontakt");
                String typ = result.getString("Typ");
                String nachricht = result.getString("Nachricht");

                if(typ=="Lerngruppe"){
                    typString = "Lerngruppe";
                } else {
                    typString = "Uebungsgruppe";}

                System.out.println(id + "|" + name + " | " + kontakt + "|" + typString + "|" + nachricht);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
            System.out.println("Error connecting to SQLite database");
        }


    }




}



