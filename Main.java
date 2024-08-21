package fr.maxime;

import fr.maxime.entity.Etudient;
import fr.maxime.repository.EtudientRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final EtudientRepository etudientRepository = new EtudientRepository();
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/jdbc?currentSchema=exo1";
        String username = "postgres";
        String password = "2203";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {

                Scanner scanner = new Scanner(System.in);
                int option = 0;
                boolean flag = true;
                while (flag) {
                    System.out.println("Connection successful");
                    System.out.println("que voulez vous faire:");
                    System.out.println("1 - ajouter un etudients");
                    System.out.println("2 - afficher les etudients");
                    System.out.println("3 - afficher les étudient par classe");
                    System.out.println("4 - supprimer un etudient");
                    option = scanner.nextInt();

                    switch (option) {
                        case 1:
                            ajouterEdutient(scanner, connection);
                            break;
                        case 2:
                            afficherAllEtudients(connection);
                            break;
                        case 3:
                            afficherAllEtudientsByClass(scanner, connection);
                            break;
                        case 4:
                            deleteEtudientsById(scanner, connection);
                            break;
                        default:
                            System.out.println("rien a etait choisi");
                            break;
                    }
                    System.out.println("voulez vous continuer ?");
                    System.out.println("1 - oui");
                    System.out.println("2 - non");
                    int continuer = scanner.nextInt();
                    flag = continuer == 1;
                }
            }
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void ajouterEdutient(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("qu'elle et son nom");
        String nom = scanner.next();
        System.out.println("qu'elle et son prenom");
        String prenom = scanner.next();
        System.out.println("qu'elle et son numero de class");
        int classe = scanner.nextInt();
        System.out.println("la date du diplome");
        String date = scanner.next();
        LocalDate dateFormate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Etudient newEtudient = Etudient.builder().nom(nom).prenom(prenom).classNumero(classe).dateDiplome(dateFormate).build();
        boolean statusAddEtudient = etudientRepository.addEtudient(newEtudient, connection);
        System.out.println(
                statusAddEtudient ?  "ajout réussi" : "ajout echouer"
        );

    }

    public static void afficherAllEtudients(Connection connection) throws SQLException {
        List<Etudient> etudientList = etudientRepository.afficherAllEtudients(connection);
        for(Etudient etudient : etudientList) {
            System.out.println(etudient.toString());
        }
    }

    public static void afficherAllEtudientsByClass(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("qu'elle est la classe choisi ?");
        int classe = scanner.nextInt();
        List<Etudient> etudientList = etudientRepository.afficherAllEtudientsByClass(classe , connection);
        for(Etudient etudient : etudientList) {
            System.out.println(etudient.toString());
        }

    }

    public static void deleteEtudientsById(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("qu'elle est l'id de l'etudient ?");
        int id = scanner.nextInt();
        boolean deleteStatus = etudientRepository.deleteEtudientsById(id, connection);
        System.out.println(
                deleteStatus ?  "suppresion réussi" : "suppression echouer"
        );
    }
}