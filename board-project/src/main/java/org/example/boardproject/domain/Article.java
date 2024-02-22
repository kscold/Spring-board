package org.example.boardproject.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter // loombok을 통해 Getter 설정
@ToString
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter // 사용할 기능에만 각 필드에 @Setter를 거는 것이 좋음
    @Column(nullable = false) // 필드가 비어있어도 됨
    private String title; // 제목

    @Setter
    @Column(nullable = false, length = 10000) // 필드가 비어있어도 되며 길이제한은 10000자
    private String content; // 본문

    @Setter
    private String hashtag; // 해시태그

    @ToString.Exclude // ArticleComment에 들어가서 또 ToString을 찍을려고 하기 때문에 제외해줌, 즉, 순환참조가 일어남
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    // 이 과정을 하지 않으면 기본 이름으로 테이블을 만들어버리기 때문에 aiticle 테이블로부터 온 것을 명시
    // 모든 종류에 대해서 연관관계 매핑 CascadeType.ALL
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt; // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; // 생성자

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt; // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자


    protected Article() { // 모든 JPA 엔티티는 hibernate를 사용하다는 기준으로 기본생성자를 꼭 가지고 있어야하고 평소에는 오픈하지 않음

    }

    private Article(String title, String content, String hashtag) { // private로 생성자 접근을 막고
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) { // 정정 생성자로 접근을 열음
        return new Article(title, content, hashtag);
    }

    // 객체 비교를 위한 equals and hashCode를 하는데, id가 유니크하기 때문에 id만 만들어주면 됨
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return id != null && id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
