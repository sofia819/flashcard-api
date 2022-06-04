package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableCreateFlashcardDeckResponse.class)
@JsonDeserialize(as = ImmutableCreateFlashcardDeckResponse.class)
public abstract class AbstractCreateFlashcardDeckResponse {
  public abstract Set<String> errors();
}
