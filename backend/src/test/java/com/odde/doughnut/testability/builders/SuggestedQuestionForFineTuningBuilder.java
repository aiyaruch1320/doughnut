package com.odde.doughnut.testability.builders;

import com.odde.doughnut.entities.Note;
import com.odde.doughnut.entities.QuizQuestionEntity;
import com.odde.doughnut.entities.SuggestedQuestionForFineTuning;
import com.odde.doughnut.testability.EntityBuilder;
import com.odde.doughnut.testability.MakeMe;

public class SuggestedQuestionForFineTuningBuilder
    extends EntityBuilder<SuggestedQuestionForFineTuning> {
  private Note note = null;
  private String preservedQuestion = null;

  public SuggestedQuestionForFineTuningBuilder(MakeMe makeMe) {
    super(makeMe, new SuggestedQuestionForFineTuning());
    entity.setUser(makeMe.aUser().please());
  }

  @Override
  protected void beforeCreate(boolean needPersist) {
    Note note = this.note == null ? makeMe.aNote().please() : this.note;
    QuizQuestionEntity quizQuestion = makeMe.aQuestion().ofNote(note).please();
    entity.setQuizQuestion(quizQuestion);
    if (this.preservedQuestion != null) {
      entity.setPreservedQuestion(this.preservedQuestion);
    } else {
      entity.setPreservedQuestion(makeMe.aMCQWithAnswer().please().toJsonString());
    }
  }

  public SuggestedQuestionForFineTuningBuilder ofNote(Note note) {
    this.note = note;
    return this;
  }

  public SuggestedQuestionForFineTuningBuilder withPreservedQuestion(String question) {
    this.preservedQuestion = question;
    return this;
  }
}