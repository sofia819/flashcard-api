## Flashcard

API: https://sofia819-flashcard-api.herokuapp.com/flashcard

### POST `/create`

- Creates a deck
- Sample body:

```json
{
  "deckName": "Deck"
}
```

### POST `/add`

- Adds flashcards to an existing deck
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

- Gets flashcards from a deck
- Sample request: `/Deck`

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