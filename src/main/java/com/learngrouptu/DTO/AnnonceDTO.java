package com.learngrouptu.DTO;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

public class AnnonceDTO {

        private String vorlName;

        private String choice;

        public AnnonceDTO(){

        }

        public AnnonceDTO(String vorlName, String choice){

            super();
            this.vorlName = vorlName;
            this.choice = choice;
        }

        //basic getter and setter
        public String getVorlName(){return vorlName;}
        public String getChoice(){return choice;}

        public void setVorlName(String vorlName){
            this.vorlName = vorlName;
        }
        public void setChoice(String choice){this.choice = choice;}

}
