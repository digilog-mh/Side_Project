package com.side.boardproject.domain;

public class ArticleComment extends BaseEntity{
    private Long id; //게시글 id
    private String content; // 내용

    private Article article; // 게시글
}
