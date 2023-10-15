// jumptoNotePage is faster than navigateToNotePage
//    it uses the note id memorized when creating them with testability api
export const jumpToNotePage = (noteTopic: string, forceLoadPage = false) => {
  cy.testability()
    .getSeededNoteIdByTitle(noteTopic)
    .then((noteId) => {
      const url = `/notes/${noteId}`
      if (forceLoadPage) cy.visit(url)
      else cy.routerPush(url, "noteShow", { noteId: noteId })
    })
  cy.findNoteTopic(noteTopic)

  return {
    startSearchingAndLinkNote() {
      cy.notePageButtonOnCurrentPage("search and link note").click()
    },
    aiGenerateImage() {
      this.clickNotePageMoreOptionsButton("Generate Image with DALL-E")
    },
    deleteNote() {
      this.clickNotePageMoreOptionsButton("Delete note")
      cy.findByRole("button", { name: "OK" }).click()
      cy.pageIsNotLoading()
    },
    clickNotePageMoreOptionsButton: (btnTextOrTitle: string) => {
      cy.clickNotePageMoreOptionsButtonOnCurrentPage(btnTextOrTitle)
    },
    associateNoteWithWikidataId(wikiID: string) {
      cy.notePageButtonOnCurrentPage("associate wikidata").click()
      cy.replaceFocusedTextAndEnter(wikiID)
    },
  }
}