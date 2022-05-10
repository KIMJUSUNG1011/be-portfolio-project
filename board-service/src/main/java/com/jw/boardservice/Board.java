package com.jw.boardservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
}
