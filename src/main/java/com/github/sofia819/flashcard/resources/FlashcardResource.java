package com.github.sofia819.flashcard.resources;

import com.codahale.metrics.annotation.Timed;
import com.github.sofia819.flashcard.api.ImmutableCreateFlashcardDeckRequest;
import com.github.sofia819.flashcard.api.ImmutableCreateFlashcardDeckResponse;
import com.github.sofia819.flashcard.api.ImmutableGetFlashcardResponse;
import com.github.sofia819.flashcard.api.ImmutableRenameDeckRequest;
import com.github.sofia819.flashcard.api.ImmutableRenameDeckResponse;
import com.github.sofia819.flashcard.api.ImmutableUpdateFlashcardRequest;
import com.github.sofia819.flashcard.api.ImmutableUpdateFlashcardResponse;
import com.github.sofia819.flashcard.db.FlashcardDao;
import java.util.Optional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
  public ImmutableCreateFlashcardDeckResponse createFlashcardDeck(
      ImmutableCreateFlashcardDeckRequest request) {
    ImmutableCreateFlashcardDeckResponse.Builder response =
        ImmutableCreateFlashcardDeckResponse.builder();

    if (!(request.deckName().length() > 0)) {
      return response.addErrors("Invalid deck name").build();
    }

    Optional<String> daoError = dao.createDeck(request.deckName());

    daoError.ifPresent(response::addErrors);

    return response.build();
  }

  @PATCH
  @Path("/rename")
  @Timed
  public ImmutableRenameDeckResponse renameDeck(ImmutableRenameDeckRequest request) {
    ImmutableRenameDeckResponse.Builder response = ImmutableRenameDeckResponse.builder();

    if (request.oldDeckName().isEmpty()) {
      response.addErrors("Invalid old deck name");
    }

    if (request.newDeckName().isEmpty()) {
      return response.addErrors("Invalid new deck name").build();
    }

    Optional<String> daoError = dao.renameDeck(request.oldDeckName(), request.newDeckName());
    daoError.ifPresent(response::addErrors);

    return response.build();
  }

  @PUT
  @Path("/update")
  @Timed
  public ImmutableUpdateFlashcardResponse updateCardsInDeck(
      ImmutableUpdateFlashcardRequest request) {
    ImmutableUpdateFlashcardResponse.Builder response = ImmutableUpdateFlashcardResponse.builder();

    if (request.deckName().isEmpty()) {
      return response.addErrors("Invalid deck name").build();
    }

    Optional<String> daoError =
        dao.updateFlashcardsInDeck(request.deckName(), request.flashcards());

    daoError.ifPresent(response::addErrors);

    return response.build();
  }

  @DELETE
  @Path("/delete/{deckName}")
  @Timed
  public String deleteDeck(@PathParam("deckName") String deckName) {
    return dao.deleteDeck(deckName).orElse("");
  }
}
