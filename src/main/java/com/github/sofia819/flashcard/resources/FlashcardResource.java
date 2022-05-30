package com.github.sofia819.flashcard.resources;

import com.codahale.metrics.annotation.Timed;
import com.github.sofia819.flashcard.api.ImmutableAddFlashcardRequest;
import com.github.sofia819.flashcard.api.ImmutableCreateFlashcardDeckRequest;
import com.github.sofia819.flashcard.api.ImmutableGetFlashcardResponse;
import com.github.sofia819.flashcard.db.FlashcardDao;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/flashcard")
@Produces(MediaType.APPLICATION_JSON)
public class FlashcardResource {
  private final FlashcardDao dao;

  public FlashcardResource() {
    this.dao = new FlashcardDao();
  }

  @GET
  @Path("/{deckName}")
  @Timed
  public ImmutableGetFlashcardResponse getFlashcardDeck(@PathParam("deckName") String deckName) {
    return ImmutableGetFlashcardResponse.builder().flashcards(dao.getFlashcards(deckName)).build();
  }

  @POST
  @Path("/create")
  @Timed
  public String createFlashcardDeck(ImmutableCreateFlashcardDeckRequest request) {
    if (request.deckName().length() > 0 && dao.createDeck(request.deckName())) {
      return "Deck created";
    }
    return "No deck created";
  }

  @POST
  @Path("/add")
  @Timed
  public String addFlashcardsToDeck(ImmutableAddFlashcardRequest request) {
    if (request.flashcards().size() > 0
        && dao.addCardsToDeck(request.deckName(), request.flashcards())) {
      return "Cards added";
    }
    return "No cards added";
  }
}
