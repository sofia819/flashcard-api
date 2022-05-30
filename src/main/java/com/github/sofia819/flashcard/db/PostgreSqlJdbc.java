package com.github.sofia819.flashcard.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PostgreSqlJdbc {

  private static Optional<Connection> connection = Optional.empty();
  private static final Logger LOGGER = Logger.getLogger("PostgreSqlJdbc");

  public static Optional<Connection> getConnection() {
    if (connection.isEmpty()) {

      try {
        connection =
            Optional.ofNullable(DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL")));
        LOGGER.log(Level.INFO, "DB connected");
      } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Cannot connect", e);
        throw new RuntimeException("Cannot connect", e);
      }
    }

    return connection;
  }
}
