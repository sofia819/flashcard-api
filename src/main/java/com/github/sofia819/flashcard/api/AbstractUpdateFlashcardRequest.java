package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.sofia819.flashcard.core.ImmutableFlashcard;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUpdateFlashcardRequest.class)
@JsonDeserialize(as = ImmutableUpdateFlashcardRequest.class)
public abstract class AbstractUpdateFlashcardRequest {
  public abstract String deckName();

  public abstract Set<ImmutableFlashcard> flashcards();
}
