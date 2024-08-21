package fr.maxime.repository;

import fr.maxime.entity.Etudient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EtudientRepository {
    public EtudientRepository() {}

    public boolean addEtudient(Etudient etudient, Connection connection) throws SQLException {
        String request = "INSERT INTO etudient (nom, prenom, classNumero, dateDiplome) VALUES (?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, etudient.getNom());
        preparedStatement.setString(2, etudient.getPrenom());
        preparedStatement.setInt(3, etudient.getClassNumero());
        preparedStatement.setDate(4, Date.valueOf(etudient.getDateDiplome()));

        ResultSet generateKey = preparedStatement.getGeneratedKeys();
        if (generateKey.next()) {
            etudient.setId(generateKey.getInt(1));
        }
        int nbRows = preparedStatement.executeUpdate();
        return nbRows > 0;
    }

    public List<Etudient> afficherAllEtudients(Connection connection) throws SQLException {
        List<Etudient> etudientList = new ArrayList<>();
        String request = "SELECT * FROM etudient";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Etudient etudient = Etudient.builder()
                    .id(resultSet.getInt("id"))
                    .nom(resultSet.getString("nom"))
                    .prenom(resultSet.getString("prenom"))
                    .classNumero(resultSet.getInt("classNumero"))
                    .dateDiplome(resultSet.getDate("dateDiplome").toLocalDate())
                    .build();
            etudientList.add(etudient);

        }
        return etudientList;
    }

    public List<Etudient> afficherAllEtudientsByClass(int classe, Connection connection) throws SQLException {
        List<Etudient> etudientList = new ArrayList<>();
        String request = "SELECT * FROM etudient WHERE classNumero = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, classe);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Etudient etudient = Etudient.builder()
                    .id(resultSet.getInt("id"))
                    .nom(resultSet.getString("nom"))
                    .prenom(resultSet.getString("prenom"))
                    .classNumero(resultSet.getInt("classNumero"))
                    .dateDiplome(resultSet.getDate("dateDiplome").toLocalDate())
                    .build();
            etudientList.add(etudient);
        }

        return etudientList;

    }

    public boolean deleteEtudientsById(int id, Connection connection) throws SQLException {
        String request = "DELETE FROM etudient WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(request);
        preparedStatement.setInt(1, id);
        int nbRows = preparedStatement.executeUpdate();
        return nbRows > 0;
    }
}
