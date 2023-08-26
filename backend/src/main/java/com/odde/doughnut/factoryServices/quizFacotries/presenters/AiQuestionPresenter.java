package com.odde.doughnut.factoryServices.quizFacotries.presenters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.odde.doughnut.entities.QuizQuestionEntity;
import com.odde.doughnut.entities.json.QuizQuestion;
import com.odde.doughnut.factoryServices.ModelFactoryService;
import com.odde.doughnut.factoryServices.quizFacotries.QuizQuestionPresenter;
import com.odde.doughnut.services.ai.AIGeneratedQuestion;
import java.util.List;

public class AiQuestionPresenter implements QuizQuestionPresenter {
  private final AIGeneratedQuestion aiQuestion;

  public AiQuestionPresenter(QuizQuestionEntity quizQuestion) {
    try {
      this.aiQuestion =
          new ObjectMapper()
              .readValue(quizQuestion.getRawJsonQuestion(), AIGeneratedQuestion.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String stem() {
    return aiQuestion.stem;
  }

  @Override
  public String mainTopic() {
    return null;
  }

  @Override
  public List<QuizQuestion.Choice> getOptions(ModelFactoryService modelFactoryService) {
    return aiQuestion.makeChoiceReasonPair().stream()
        .map(
            pair -> {
              QuizQuestion.Choice option = new QuizQuestion.Choice();
              return option.getChoice(pair);
            })
        .toList();
  }
}
