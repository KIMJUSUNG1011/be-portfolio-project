package com.jw.boardservice.board;

import com.jw.boardservice.BaseTimeEntity;
import com.jw.boardservice.comment.Comment;
import com.jw.boardservice.file.FileEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String email;
    private int count;

    @OneToMany(mappedBy = "board")
    private List<FileEntity> files = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    @OrderBy("id ASC")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String title, String content, String email)
    {
        this.title = title;
        this.content = content;
        this.email = email;
    }

    public void update(Board board)
    {
        this.title = board.title;
        this.content = board.content;
    }

    public void increaseViewCount()
    {
        this.count = this.count + 1;
    }

    public void addFile(FileEntity fileEntity)
    {
        files.add(fileEntity);
        fileEntity.mapBoard(this);
    }

    public void addComment(Comment comment)
    {
        comments.add(comment);
        comment.mapBoard(this);
    }
}
