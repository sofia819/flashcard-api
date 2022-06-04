package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableRenameDeckRequest.class)
@JsonDeserialize(as = ImmutableRenameDeckRequest.class)
public abstract class AbstractRenameDeckRequest {
  public abstract String oldDeckName();

  public abstract String newDeckName();
}
