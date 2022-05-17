package com.jw.boardservice.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jw.boardservice.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FileEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonIgnore
    private Board board;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String path;

    private Long size;

    public void mapBoard(Board board) {
        this.board = board;
    }
}
