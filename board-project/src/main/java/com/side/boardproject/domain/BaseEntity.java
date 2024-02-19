package com.side.boardproject.domain;

import java.time.LocalDateTime;

public class BaseEntity {
    private LocalDateTime createAt; //생성일시
    private String createBy; //생성자
    private LocalDateTime modifiedAt; //수정일시
    private String modifiedBy; //수정자
}
