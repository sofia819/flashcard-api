package com.github.sofia819.flashcard.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableFlashcard.class)
@JsonDeserialize(as = ImmutableFlashcard.class)
public abstract class AbstractFlashcard {
  @JsonProperty
  public abstract String question();

  @JsonProperty
  public abstract String answer();
}
