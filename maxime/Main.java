package fr.maxime;

import fr.maxime.entity.Etudient;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
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

        String request = "INSERT INTO etudient (nom, prenom, classNumero, dateDiplome) VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, newEtudient.getNom());
        preparedStatement.setString(2, newEtudient.getPrenom());
        preparedStatement.setInt(3, newEtudient.getClassNumero());
        preparedStatement.setDate(4, Date.valueOf(newEtudient.getDateDiplome()));

        ResultSet generateKey = preparedStatement.getGeneratedKeys();
        if (generateKey.next()) {
            newEtudient.setId(generateKey.getInt(1));
        }
        int nbRows = preparedStatement.executeUpdate();
        if (nbRows > 0) {
            System.out.println("etudient ajouter");
        }
    }

    public static void afficherAllEtudients(Connection connection) throws SQLException {
        String request = "SELECT * FROM etudient";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("---- etudient " + resultSet.getInt("id") + " ----");
            System.out.println("nom:           " + resultSet.getString("nom"));
            System.out.println("prenom:        " + resultSet.getString("prenom"));
            System.out.println("numero classe: " + resultSet.getInt("classNumero"));
            System.out.println("date diplome:  " + resultSet.getDate("dateDiplome"));
            System.out.println("--------------------");
        }
    }

    public static void afficherAllEtudientsByClass(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("qu'elle est la classe choisi ?");
        String classe = scanner.next();
        String request = "SELECT * FROM etudient WHERE classNumero = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, Integer.parseInt(classe));
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            System.out.println("---- etudient " + resultSet.getInt("id") + " ----");
            System.out.println("nom:           " + resultSet.getString("nom"));
            System.out.println("prenom:        " + resultSet.getString("prenom"));
            System.out.println("numero classe: " + resultSet.getInt("classNumero"));
            System.out.println("date diplome:  " + resultSet.getDate("dateDiplome"));
            System.out.println("--------------------");
        }

    }

    public static void deleteEtudientsById(Scanner scanner, Connection connection) throws SQLException {
        System.out.println("qu'elle est l'id de l'etudient ?");
        int id = scanner.nextInt();
        String request = "DELETE FROM etudient WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        int nbRows = preparedStatement.executeUpdate();
        if (nbRows > 0) {
            System.out.println("etudient deleted");
        }
    }
}