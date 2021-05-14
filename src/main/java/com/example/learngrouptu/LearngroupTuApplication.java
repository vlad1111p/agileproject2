package com.example.learngrouptu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.sql.*;
//inport java.SQLException;

@SpringBootApplication
@RestController
public class LearngroupTuApplication {

    public static void main(String[] args) {
        String typString = null;
        SpringApplication.run(LearngroupTuApplication.class, args);

        String jdbcUrl = "jdbc:sqlite:./LearngroupTu_DB.db";

        //String jdbcUrl = "LearngroupTu_DB_new.db";


        try{

            /*
                //TODO new einbinden
             */


            Connection connection = DriverManager.getConnection(jdbcUrl);
            String sql =" SELECT * FROM table_userinfo";

            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                Integer id = result.getInt("ID");
                String name = result.getString("Vorlesung");
                String kontakt = result.getString("Kontakt");
                Integer typ = result.getInt("Typ");
                String nachricht = result.getString("Nachricht");

                if(typ==1){
                    typString = "Lerngruppe";
                } else {
                    typString = "Uebungsgruppe";}

                System.out.println(id + "|" + name + " | " + kontakt + "|" + typString + "|" + nachricht);
            }

        } catch (SQLException e){
            System.out.println("Error connecting to SQLite database");
        }


    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue =  "World") String name) {
        return String.format("Hello %s!", name);
    }

}
