package com.odde.doughnut.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class UserEntity {

    @Id @Getter @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Getter @Setter private String name;
    @Column(name = "external_identifier") @Getter @Setter private String externalIdentifier;

    @OneToMany(mappedBy = "userEntity")
    @JsonIgnore
    @Getter @Setter private List<NoteEntity> notes = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity")
    @JsonIgnore
    @Getter @Setter private List<ReviewPointEntity> reviewPoints = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity")
    @Where(clause = "parent_id IS NULL")
    @JsonIgnore
    @Getter private List<NoteEntity> orphanedNotes;

    @Column(name = "daily_new_notes_count") @Getter @Setter private Integer dailyNewNotesCount = 10;

    @Column(name = "space_intervals") @Getter @Setter private String spaceIntervals = "1, 2, 3, 5, 8, 13, 21, 34, 55";

    public boolean owns(NoteEntity note) {
        return note.getUserEntity().id.equals(id);
    }

}
