## Flashcard

API: https://sofia819-flashcard-api.herokuapp.com/flashcard

### POST `/create`

- Creates a deck from a given deck name
- Sample body:

```json
{
  "deckName": "Deck"
}
```

### POST `/add`

- Adds flashcards to an existing deck, given a deck name
- Sample request:

```json
{
  "deckName": "Deck",
  "flashcards": [
    {
      "question": "How are you?",
      "answer": "I am doing well."
    }
  ]
}
```

### GET `/{deckName}`

- Gets flashcards from a deck, given a deck name
- Sample request: `/Deck`
- Sample response:

```json
{
  "flashcards": [
    {
      "question": "How are you?",
      "answer": "I am doing well."
    }
  ]
}
```

## Running Locally

1. Install JDK
2. Install PostgreSQL
3. Set up local PostgreSQL user and database
4. Set up an environment variable `JDBC_DATABASE_URL` in the following format:

```ignorelang
jdbc:postgresql://<host>:<port>/<database>?user=<user>&password=<password>
```

5. Run in your command line the following:

Windows:

```ignorelang
mvn package
java -jar target/flashcard-api-1.0-SNAPSHOT.jar server
```

Mac:

```ignorelang
mvn package
java -jar target\flashcard-api-1.0-SNAPSHOT.jar server
```