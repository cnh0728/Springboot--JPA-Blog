package com.ramyun.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob //대용량 데이터
    private String content;

    private int count;

    //EAGER = 무조건 들고오기 LAZY = 미뤘다가 필요할 때 들고오기
    @ManyToOne(fetch = FetchType.EAGER) // Many = Board, One = User
    @JoinColumn(name = "userId")
    private User user; // DB는 오브젝트를 저장할 수 없지만 자바는 가능하다.

    @OneToMany(mappedBy = "board", fetch = FetchType.EAGER) // mappedBy 연관관계의 주인이 아니니까 FK키를 만들지 마세요.
    @JsonIgnoreProperties({"board"}) //무한참조 방지 board<->replies
    private List<Reply> replies;

    @CreationTimestamp
    private Timestamp createDate;
}
