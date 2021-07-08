package com.odde.doughnut.factoryServices;

import com.odde.doughnut.entities.*;
import com.odde.doughnut.entities.repositories.*;
import com.odde.doughnut.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Service
public class ModelFactoryService {
    @Autowired public NoteRepository noteRepository;
    @Autowired public UserRepository userRepository;
    @Autowired public BazaarNotebookRepository bazaarNotebookRepository;
    @Autowired public ReviewPointRepository reviewPointRepository;
    @Autowired public CircleRepository circleRepository;
    @Autowired public LinkRepository linkRepository;
    @Autowired public NotesClosureRepository notesClosureRepository;
    @Autowired public NotebookRepository notebookRepository;
    @Autowired public EntityManager entityManager;
    @Autowired public FailureReportRepository failureReportRepository;

    public NoteModel toNoteModel(Note note) {
        return new NoteModel(note, this);
    }

    public NoteMotionModel toNoteMotionModel(NoteMotion noteMotion, Note note) {
        noteMotion.setSubject(note);
        return new NoteMotionModel(noteMotion, this);
    }

    public NoteMotionModel toNoteMotionModel(Note sourceNote, Note targetNote, Boolean asFirstChild) {
        if(!asFirstChild) {
            List<Note> children = targetNote.getChildren();
            if (children.size()> 0) {
                return toNoteMotionModel(new NoteMotion(children.get(children.size() - 1), false), sourceNote);
            }
        }
        return toNoteMotionModel(new NoteMotion(targetNote, true), sourceNote);
    }

    public BazaarModel toBazaarModel() {
        return new BazaarModel(this);
    }
    public BlogModel toBlogModel(Notebook notebook) {
        return new BlogModel(notebook);
    }

    public Optional<User> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<Note> findNoteById(Integer noteId) {
        return noteRepository.findById(noteId);
    }

    public UserModel toUserModel(User user) {
        return new UserModel(user, this);
    }

    public ReviewPointModel toReviewPointModel(@Valid ReviewPoint reviewPoint) {
        return new ReviewPointModel(reviewPoint, this);
    }

    public CircleModel toCircleModel(Circle circle) {
        return new CircleModel(circle, this);
    }

    public CircleModel findCircleByInvitationCode(String invitationCode) {
        Circle circle = circleRepository.findFirstByInvitationCode(invitationCode);
        if (circle == null) {
            return null;
        }
        return toCircleModel(circle);
    }

    public LinkModel toLinkModel(Link link) {
        return new LinkModel(link, this);
    }

    public SubscriptionModel toSubscriptionModel(Subscription sub) {
        return new SubscriptionModel(sub, this);
    }

    public Authorization toAuthorization(User entity) {
        return new Authorization(entity, this);
    }

}
