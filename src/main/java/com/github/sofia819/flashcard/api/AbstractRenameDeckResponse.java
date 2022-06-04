package com.github.sofia819.flashcard.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.Set;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableRenameDeckResponse.class)
@JsonDeserialize(as = ImmutableRenameDeckResponse.class)
public abstract class AbstractRenameDeckResponse {
  public abstract Set<String> errors();
}
