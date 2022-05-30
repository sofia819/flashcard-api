package com.github.sofia819.flashcard.db;

import com.github.sofia819.flashcard.core.ImmutableFlashcard;
import com.google.common.collect.ImmutableSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlashcardDao {

  private static final Logger LOGGER = Logger.getLogger("FlashcardDao");
  private final Optional<Connection> connection;

  public FlashcardDao() {
    this.connection = PostgreSqlJdbc.getConnection();
    dropTables();
    if (!createTables()) {
      throw new RuntimeException("Tables not created!");
    }
  }

  public boolean createDeck(String deckName) {
    String sql = "INSERT INTO flashcard_deck(name) VALUES (?)";

    return connection
        .flatMap(
            conn -> {
              try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, deckName);
                statement.execute();
              } catch (SQLException e) {
                e.printStackTrace();
                return Optional.of(e.toString());
              }

              return Optional.empty();
            })
        .isEmpty();
  }

  public boolean addCardsToDeck(String deckName, Set<ImmutableFlashcard> flashcards) {
    Optional<Integer> deckId = getDeckId(deckName);

    return deckId.filter(integer -> addFlashcards(integer, flashcards).isEmpty()).isPresent();
  }

  private Optional<Integer> getDeckId(String deckName) {
    String createDeckSql = "SELECT deck_id FROM flashcard_deck WHERE name = ?;";

    return connection.flatMap(
        conn -> {
          try (PreparedStatement statement = conn.prepareStatement(createDeckSql)) {
            statement.setString(1, deckName);
            statement.execute();

            ResultSet createdDeck = statement.getResultSet();
            if (createdDeck.next()) {
              return Optional.of(createdDeck.getInt(1));
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }

          return Optional.empty();
        });
  }

  private Optional<String> addFlashcards(int deckId, Set<ImmutableFlashcard> flashcards) {
    String createFlashcardSql =
        "INSERT INTO flashcard(deck_id, question, answer) VALUES (?, ?, ?);";

    return connection.flatMap(
        conn -> {
          try (PreparedStatement statement = conn.prepareStatement(createFlashcardSql)) {

            for (ImmutableFlashcard flashcard : flashcards) {
              statement.setInt(1, deckId);
              statement.setString(2, flashcard.question());
              statement.setString(3, flashcard.answer());
            }
            statement.execute();
          } catch (SQLException e) {
            e.printStackTrace();
            return Optional.of(e.toString());
          }

          return Optional.empty();
        });
  }

  public Set<ImmutableFlashcard> getFlashcards(String deckName) {
    String sql =
        "SELECT question, answer FROM flashcard WHERE deck_id = ("
            + "SELECT deck_id FROM flashcard_deck WHERE name = ?)";

    ImmutableSet.Builder<ImmutableFlashcard> flashcards = ImmutableSet.builder();

    connection.flatMap(
        conn -> {
          try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, deckName);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
              String question = resultSet.getString(1);
              String answer = resultSet.getString(2);
              flashcards.add(
                  ImmutableFlashcard.builder().question(question).answer(answer).build());
            }

          } catch (SQLException e) {
            e.printStackTrace();
          }
          return Optional.empty();
        });

    return flashcards.build();
  }

  private boolean createTables() {
    String flashcardDeckTableCreateSql =
        "CREATE TABLE IF NOT EXISTS flashcard_deck ("
            + "deck_id SERIAL PRIMARY KEY NOT NULL,"
            + "name TEXT NOT NULL );";

    Optional<String> flashcardDeckError =
        connection.flatMap(
            conn -> {
              try (PreparedStatement statement =
                  conn.prepareStatement(flashcardDeckTableCreateSql)) {
                statement.execute();
              } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to create flashcard deck table", e);
                return Optional.of(e.toString());
              }

              return Optional.empty();
            });

    if (flashcardDeckError.isEmpty()) {
      String flashcardTableCreateSql =
          "CREATE TABLE IF NOT EXISTS flashcard ("
              + "card_id SERIAL PRIMARY KEY NOT NULL,"
              + "deck_id INT,"
              + "question TEXT NOT NULL,"
              + "answer TEXT NOT NULL,"
              + "CONSTRAINT fk_deck_id FOREIGN KEY(deck_id) REFERENCES flashcard_deck(deck_id)"
              + ");";

      Optional<String> flashcardError =
          connection.flatMap(
              conn -> {
                try (PreparedStatement statement = conn.prepareStatement(flashcardTableCreateSql)) {
                  statement.execute();
                } catch (SQLException e) {
                  LOGGER.log(Level.SEVERE, "Failed to create flashcard table", e);
                  return Optional.of(e.toString());
                }

                return Optional.empty();
              });

      if (flashcardError.isEmpty()) {
        return true;
      }
    }

    dropTables();
    return false;
  }

  private void dropTables() {
    String dropFlashcardTableSql = "DROP TABLE IF EXISTS flashcard;";

    connection.flatMap(
        conn -> {
          try (PreparedStatement statement = conn.prepareStatement(dropFlashcardTableSql)) {
            statement.execute();
          } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to drop flashcard deck table", e);
            return Optional.of(e.toString());
          }

          return Optional.empty();
        });

    String dropDeckTableSql = "DROP TABLE IF EXISTS flashcard_deck;";
    connection.flatMap(
        conn -> {
          try (PreparedStatement statement = conn.prepareStatement(dropDeckTableSql)) {
            statement.execute();
          } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to drop flashcard deck table", e);
            return Optional.of(e.toString());
          }

          return Optional.empty();
        });
  }
}
