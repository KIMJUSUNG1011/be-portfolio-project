package com.jw.boardservice.board;

import com.jw.boardservice.BaseTimeEntity;
import com.jw.boardservice.file.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "board")
    private List<FileEntity> files = new ArrayList<>();

    private String title;
    private String content;
    private String email;
    private int count;

    public void update(Board board)
    {
        this.title = board.title;
        this.content = board.content;
    }

    public void increaseCount()
    {
        this.count = this.count + 1;
    }

    public void addFile(FileEntity fileEntity) {
        files.add(fileEntity);
        fileEntity.mapBoard(this);
    }
}
