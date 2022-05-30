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