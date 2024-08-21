package fr.maxime.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Etudient {
    private int id ;
    private String nom ;
    private String prenom ;
    private int classNumero;
    private LocalDate dateDiplome;
}
