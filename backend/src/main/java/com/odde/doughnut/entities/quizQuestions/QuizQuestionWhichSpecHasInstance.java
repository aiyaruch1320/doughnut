package com.odde.doughnut.entities.quizQuestions;

import com.odde.doughnut.entities.QuizQuestionEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("9")
public class QuizQuestionWhichSpecHasInstance extends QuizQuestionEntity {

  public String getMainTopic() {
    return null;
  }
}
