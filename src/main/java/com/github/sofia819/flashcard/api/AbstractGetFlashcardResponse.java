package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.sofia819.flashcard.core.ImmutableFlashcard;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableGetFlashcardResponse.class)
@JsonDeserialize(as = ImmutableGetFlashcardResponse.class)
public abstract class AbstractGetFlashcardResponse {
  public abstract Set<ImmutableFlashcard> flashcards();
}
