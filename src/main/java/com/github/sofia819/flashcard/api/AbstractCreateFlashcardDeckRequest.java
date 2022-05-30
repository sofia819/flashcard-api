package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateFlashcardDeckRequest.class)
@JsonDeserialize(as = ImmutableCreateFlashcardDeckRequest.class)
public abstract class AbstractCreateFlashcardDeckRequest {
  public abstract String deckName();
}
