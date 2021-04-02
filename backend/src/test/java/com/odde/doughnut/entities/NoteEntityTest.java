package com.odde.doughnut.entities;

import com.odde.doughnut.services.ModelFactoryService;
import com.odde.doughnut.testability.MakeMe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.odde.doughnut.entities.LinkEntity.LinkType.*;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:repository.xml"})
@Transactional
public class NoteEntityTest {

    @Autowired MakeMe makeMe;
    UserEntity userEntity;

    @Test
    void timeOrder() {
        NoteEntity parent = makeMe.aNote().please();
        NoteEntity note1 = makeMe.aNote().under(parent).please();
        NoteEntity note2 = makeMe.aNote().under(parent).please();
        makeMe.refresh(parent);
        assertThat(parent.getChildren(), containsInRelativeOrder(note1, note2));
    }

    @Nested
    class Picture {

        @Test
        void useParentPicture() {
            NoteEntity parent = makeMe.aNote().pictureUrl("https://img.com/xxx.jpg").inMemoryPlease();
            NoteEntity child = makeMe.aNote().under(parent).useParentPicture().inMemoryPlease();
            assertThat(child.getNotePicture(), equalTo(parent.getNoteContent().getPictureUrl()));
        }
    }

    @Nested
    class ValidationTest {
        private Validator validator;
        private final NoteEntity note = makeMe.aNote().inMemoryPlease();

        @BeforeEach
        public void setUp() {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @Test
        public void defaultNoteFromMakeMeIsValidate() {
            assertThat(getViolations(), is(empty()));
        }

        @Test
        public void titleIsNotOptional() {
            makeMe.theNote(note).title("");
            assertThat(getViolations(), is(not(empty())));
        }

        @Test
        public void titleCannotBeTooLong() {
            makeMe.theNote(note).title(makeMe.aStringOfLength(101));
            assertThat(getViolations(), is(not(empty())));
        }

        @Test
        public void goodMask() {
            note.getNoteContent().setPictureMask("1 -2.3 3 -4");
            assertThat(getViolations(), is(empty()));
        }

        @Test
        public void goodMaskWith2Rect() {
            note.getNoteContent().setPictureMask("-1 2 3 4 11 22 33 44");
            assertThat(getViolations(), is(empty()));
        }

        @Test
        public void masksNeedToBeFourNumbers() {
            note.getNoteContent().setPictureMask("1 2 3 4 5 6 7");
            assertThat(getViolations(), is(not(empty())));
            Path propertyPath = getViolations().stream().findFirst().get().getPropertyPath();
            assertThat(propertyPath.toString(), equalTo("noteContent.pictureMask"));
        }

        @Test
        public void withBothUploadPictureProxyAndPicture() {
            note.getNoteContent().setUploadPictureProxy(makeMe.anUploadedPicture().toMultiplePartFilePlease());
            note.getNoteContent().setPictureUrl("http://url/img");
            assertThat(getViolations(), is(not(empty())));
            List<String> errorFields = getViolations().stream().map(v->v.getPropertyPath().toString()).collect(toList());
            assertThat(errorFields, containsInAnyOrder("noteContent.uploadPicture", "noteContent.pictureUrl"));
        }

        private Set<ConstraintViolation<NoteEntity>> getViolations() {
            return validator.validate(note);
        }

    }

    @Nested
    class NoteEntityWithUserEntity {
        @Autowired private ModelFactoryService modelFactoryService;

        @BeforeEach
        void setup() {
            userEntity = makeMe.aUser().with2Notes().please();
        }

        @Test
        void thereShouldBe2NodesForUser() {
            List<NoteEntity> notes = userEntity.getNotes();
            assertThat(notes, hasSize(equalTo(2)));
        }

        @Test
        void targetIsEmptyByDefault() {
            NoteEntity note = userEntity.getNotes().get(0);
            assertThat(note.getTargetNotes(), is(empty()));
        }

        @Test
        void targetOfLinkedNotes() {
            NoteEntity note = userEntity.getNotes().get(0);
            NoteEntity targetNote = userEntity.getNotes().get(1);
            makeMe.theNote(note).linkTo(targetNote).please();
            List<NoteEntity> targetNotes = note.getTargetNotes();
            assertThat(targetNotes, hasSize(equalTo(1)));
            assertThat(targetNotes, contains(targetNote));
        }
    }

    @Nested
    class LinkTypes {
        NoteEntity noteEntityA;
        NoteEntity noteEntityB;

        @BeforeEach
        void setup() {
            noteEntityA = makeMe.aNote().please();
            noteEntityB = makeMe.aNote().please();
        }

        @Nested
        class Related {
            @BeforeEach
            void setup() {
                makeMe.theNote(noteEntityA).linkTo(noteEntityB, RELATED_TO, null).please();
            }


            @Test
            void AIsRelatedToB() {
                assertThat(noteEntityA.linkTypes(), contains(RELATED_TO));
                assertThat(getLinkedNoteOfLinkType(LinkTypes.this.noteEntityA, RELATED_TO), contains(noteEntityB));
            }

            @Test
            void BIsAlsoRelatedToA() {
                assertThat(noteEntityB.linkTypes(), contains(RELATED_TO));
                assertThat(getLinkedNoteOfLinkType(noteEntityB, RELATED_TO), contains(noteEntityA));
            }

        }

        @Nested
        class BelongsTo {
            @BeforeEach
            void setup() {
                makeMe.theNote(noteEntityA).linkTo(noteEntityB, BELONGS_TO, null).please();
            }

            @Test
            void ABelongToB() {
                assertThat(noteEntityA.linkTypes(), contains(BELONGS_TO));
                assertThat(getLinkedNoteOfLinkType(noteEntityA, BELONGS_TO), contains(noteEntityB));
            }

            @Test
            void BHasA() {
                assertThat(noteEntityB.linkTypes(), contains(HAS));
                assertThat(getLinkedNoteOfLinkType(noteEntityB, HAS), contains(noteEntityA));
            }

        }

        @Nested
        class Has {
            @BeforeEach
            void setup() {
                makeMe.theNote(noteEntityA).linkTo(noteEntityB, HAS, null).please();
            }

            @Test
            void AHasB() {
                assertThat(noteEntityA.linkTypes(), contains(HAS));
                assertThat(getLinkedNoteOfLinkType(noteEntityA, HAS), contains(noteEntityB));
            }

            @Test
            void BBelongsToA() {
                assertThat(noteEntityB.linkTypes(), contains(BELONGS_TO));
                assertThat(getLinkedNoteOfLinkType(noteEntityB, BELONGS_TO), contains(noteEntityA));
            }

        }

    }

    private List<NoteEntity> getLinkedNoteOfLinkType(NoteEntity noteEntityA, LinkEntity.LinkType relatedTo) {
        List<NoteEntity> direct = noteEntityA.linksOfTypeThroughDirect(relatedTo).stream().map(LinkEntity::getTargetNote).collect(toList());
        List<NoteEntity> reverse = noteEntityA.linksOfTypeThroughReverse(relatedTo).stream().map(LinkEntity::getSourceNote).collect(Collectors.toUnmodifiableList());
        direct.addAll(reverse);
        return direct;
    }
}
