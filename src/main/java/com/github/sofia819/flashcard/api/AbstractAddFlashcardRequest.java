package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.sofia819.flashcard.core.ImmutableFlashcard;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableAddFlashcardRequest.class)
@JsonDeserialize(as = ImmutableAddFlashcardRequest.class)
public abstract class AbstractAddFlashcardRequest {
  public abstract String deckName();

  public abstract Set<ImmutableFlashcard> flashcards();
}
