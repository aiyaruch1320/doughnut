Feature: Delete a comment from a note
  As a user, I want to delete a comment from an existing note

  Background:
    Given I've logged in as an existing user
    And there are some notes for the current user
      | title          | testingParent  | description         | author |
      | LeSS in Action |                | An awesome training | a      |
      | team           | LeSS in Action |                     | b      |
      | tech           | LeSS in Action |                     | c      |

  @ignore
  Scenario: Remove a comment from a note

    When I select my note with title 'LeSS in Action'
    Then I should see comment added to note
    And I click the delete comment button
    Then I should not see comment with value "comment to be deleted"
