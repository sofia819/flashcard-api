package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableUpdateFlashcardResponse.class)
@JsonDeserialize(as = ImmutableUpdateFlashcardResponse.class)
public abstract class AbstractUpdateFlashcardResponse {
  public abstract Set<String> errors();
}
