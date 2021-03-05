import {
  Given,
  And,
  Then,
  When,
  Before,
} from "cypress-cucumber-preprocessor/steps";

Then("I do these initial reviews in sequence:", (data) => {
  cy.visit('/reviews/initial');
  data.hashes().forEach(initialReview => {
    cy.initialReviewOneNoteIfThereIs(initialReview);
  });
})

Given("It's day {int}, {int} hour", (day, hour) => {
    cy.timeTravelTo(day, hour);
});

Then("I should be able to follow these review actions:", (data) => {
  data.hashes().forEach(({day, old_notes_to_review, initial_review}) => {
    cy.timeTravelTo(day, 8);

    cy.visit('/reviews/repeat');
    old_notes_to_review.commonSenseSplit(", ").forEach(noteIndex => {
        const [review_type, title] = ["single note", `Note ${noteIndex}`];
        cy.repeatReviewOneNoteIfThereIs({review_type, title});
    });

    cy.visit('/reviews/initial');
    initial_review.commonSenseSplit(", ").forEach(noteIndex => {
        const [review_type, title] = ["single note", `Note ${noteIndex}`];
        cy.initialReviewOneNoteIfThereIs({review_type, title});
    });

  });
});

