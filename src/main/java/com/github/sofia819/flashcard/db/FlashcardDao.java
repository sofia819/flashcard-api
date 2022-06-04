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
  private final Connection connection;

  public FlashcardDao() {
    this.connection = PostgreSqlJdbc.getConnection();
    dropTables();
    if (!createTables()) {
      throw new RuntimeException("Tables not created!");
    }
  }

  public Optional<String> createDeck(String deckName) {
    String createDeckSql = "INSERT INTO deck(name) VALUES (?)";

    try (PreparedStatement statement = connection.prepareStatement(createDeckSql)) {
      statement.setString(1, deckName);
      statement.execute();
    } catch (SQLException e) {
      return Optional.of(e.toString());
    }

    return Optional.empty();
  }

  public Optional<String> renameDeck(String oldDeckName, String newDeckName) {
    String renameDeckSql = "UPDATE deck SET name = ? WHERE name = ?";

    try (PreparedStatement statement = connection.prepareStatement(renameDeckSql)) {
      statement.setString(1, newDeckName);
      statement.setString(2, oldDeckName);
      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.of(e.toString());
    }

    return Optional.empty();
  }

  public Optional<String> updateFlashcardsInDeck(
      String deckName, Set<ImmutableFlashcard> flashcards) {
    Optional<Integer> deckId = getDeckId(deckName);

    if (deckId.isPresent()) {
      Optional<String> deleteError = deleteFlashcards(deckId.get());

      if (flashcards.size() <= 0) {
        return Optional.empty();
      }

      if (deleteError.isEmpty()) {
        return insertFlashcards(deckId.get(), flashcards);
      }
    }

    return Optional.of("No deck found");
  }

  public Set<ImmutableFlashcard> getFlashcards(String deckName) {
    String getFlashcardsSql =
        "SELECT question, answer FROM flashcard WHERE deck_id = ("
            + "SELECT deck_id FROM deck WHERE name = ?)";

    ImmutableSet.Builder<ImmutableFlashcard> flashcards = ImmutableSet.builder();

    try (PreparedStatement statement = connection.prepareStatement(getFlashcardsSql)) {
      statement.setString(1, deckName);

      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next()) {
        String question = resultSet.getString(1);
        String answer = resultSet.getString(2);
        flashcards.add(ImmutableFlashcard.builder().question(question).answer(answer).build());
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return flashcards.build();
  }

  public Optional<String> deleteDeck(String deckName) {
    Optional<Integer> deckId = getDeckId(deckName);

    if (deckId.isPresent()) {
      Optional<String> deleteError = deleteFlashcards(deckId.get());
      if (deleteError.isEmpty()) {
        return deleteDeckByDeckId(deckId.get());
      }
    }

    return Optional.of("No deck found");
  }

  private Optional<Integer> getDeckId(String deckName) {
    String getDeckIdSql = "SELECT deck_id FROM deck WHERE name = ?;";

    try (PreparedStatement statement = connection.prepareStatement(getDeckIdSql)) {
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
  }

  private Optional<String> deleteDeckByDeckId(int deckId) {
    String sql = "DELETE FROM deck WHERE deck_id = ?";

    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, deckId);
      statement.execute();
    } catch (SQLException e) {
      return Optional.of(e.toString());
    }

    return Optional.empty();
  }

  private Optional<String> deleteFlashcards(int deckId) {
    String deleteFlashcardsSql = "DELETE FROM flashcard WHERE deck_id = ?";

    try (PreparedStatement statement = connection.prepareStatement(deleteFlashcardsSql)) {
      statement.setInt(1, deckId);

      statement.execute();
    } catch (SQLException e) {
      e.printStackTrace();
      return Optional.of(e.toString());
    }

    return Optional.empty();
  }

  private Optional<String> insertFlashcards(int deckId, Set<ImmutableFlashcard> flashcards) {
    String insertFlashcardsSql =
        "INSERT INTO flashcard(deck_id, question, answer) VALUES (?, ?, ?);";

    try (PreparedStatement statement = connection.prepareStatement(insertFlashcardsSql)) {

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
  }

  private boolean createTables() {
    String createDeckTableSql =
        "CREATE TABLE IF NOT EXISTS deck ("
            + "deck_id SERIAL PRIMARY KEY NOT NULL,"
            + "name TEXT NOT NULL UNIQUE);";

    String flashcardDeckError = null;

    try (PreparedStatement statement = connection.prepareStatement(createDeckTableSql)) {
      statement.execute();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Failed to create flashcard deck table", e);
      flashcardDeckError = e.toString();
    }

    if (flashcardDeckError != null) {
      return false;
    }

    String createFlashcardTableSql =
        "CREATE TABLE IF NOT EXISTS flashcard ("
            + "card_id SERIAL PRIMARY KEY NOT NULL,"
            + "deck_id INT,"
            + "question TEXT NOT NULL,"
            + "answer TEXT NOT NULL,"
            + "CONSTRAINT fk_deck_id FOREIGN KEY(deck_id) REFERENCES deck(deck_id)"
            + ");";

    String flashcardError = null;

    try (PreparedStatement statement = connection.prepareStatement(createFlashcardTableSql)) {
      statement.execute();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Failed to create flashcard table", e);
      flashcardError = e.toString();
    }

    if (flashcardError == null) {
      return true;
    }

    dropTables();
    return false;
  }

  private void dropTables() {
    String dropFlashcardTableSql = "DROP TABLE IF EXISTS flashcard;";
    try (PreparedStatement statement = connection.prepareStatement(dropFlashcardTableSql)) {
      statement.execute();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Failed to drop flashcard deck table", e);
    }
    ;

    String dropDeckTableSql = "DROP TABLE IF EXISTS deck;";
    try (PreparedStatement statement = connection.prepareStatement(dropDeckTableSql)) {
      statement.execute();
    } catch (SQLException e) {
      LOGGER.log(Level.SEVERE, "Failed to drop flashcard deck table", e);
    }
  }
}
