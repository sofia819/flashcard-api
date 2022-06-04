package com.github.sofia819.flashcard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlJdbc {

  private static Connection connection = null;
  private static final Logger LOGGER = Logger.getLogger("PostgreSqlJdbc");

  public static Connection getConnection() {
    if (connection == null) {

      try {
        connection = DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
        LOGGER.log(Level.INFO, "DB connected");
      } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Cannot connect to DB", e);
        throw new RuntimeException(e);
      }
    }

    return connection;
  }
}
