package com.github.sofia819.flashcard.application;

import com.github.sofia819.flashcard.configuration.FlashcardConfiguration;
import com.github.sofia819.flashcard.resources.FlashcardResource;
import io.dropwizard.Application;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Environment;

public class FlashcardApplication extends Application<FlashcardConfiguration> {

  public static void main(String[] args) throws Exception {
    new FlashcardApplication().run(args);
  }

  @Override
  public void run(FlashcardConfiguration configuration, Environment environment) throws Exception {
    final FlashcardResource resource = new FlashcardResource();
    environment.jersey().register(resource);
    environment.jersey().register(new JsonProcessingExceptionMapper(true));
  }
}
