package com.example.demo.service;

import com.example.demo.models.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    private Connection connection;

    // Constructor to connect to SQLite database
    public DatabaseService() throws SQLException {
        String dbPath = "C:\\Users\\Анастасія\\Desktop\\hw.sql";
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        createTableIfNotExists();
    }

    // Create table if it does not exist
    private void createTableIfNotExists() throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "content TEXT NOT NULL," +
                "deadline DATETIME);";
        statement.executeUpdate(sql);
    }

    // Insert a new task into the database
    public void addTask(String content, String deadline) throws SQLException {
        String sql = "INSERT INTO [Homework](content, deadline) VALUES (?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, content);
        statement.setString(2, deadline);
        statement.executeUpdate();
    }

    // Get all tasks from the database
    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<Task>();
        String sql = "SELECT * FROM [Homework]";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String content = resultSet.getString("content");
            String deadline = resultSet.getString("deadline");
            Task task = new Task(id, content, deadline);
            tasks.add(task);
        }
        return tasks;
    }

    // Update an existing task in the database
    public void updateTask(int id, String content, String deadline) throws SQLException {
        String sql = "UPDATE [Homework] SET content=?, deadline=? WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, content);
        statement.setString(2, deadline);
        statement.setInt(3, id);
        statement.executeUpdate();
    }

    // Delete a task from the database
    public void deleteTask(int id) throws SQLException {
        String sql = "DELETE FROM [Homework] WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    // Close the database connection
    public void close() throws SQLException {
        connection.close();
    }
}


