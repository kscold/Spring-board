package org.example.boardproject.repository;

import org.example.boardproject.config.JpaConfig;
import org.example.boardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // 오디팅을 사용중이기 때문에 인식할 수 있도록 설정 클래스를 임포트해줌
@DataJpaTest // 이 어노테이션을 붙임으로써 @Autowired를 사용하지 않고
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;


    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository) {
        // 생성자 주입
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count(); // 총 테스트 데이터의 갯수 반환

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring")); // 하나의 임의의 데이터를 넣음

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1); // 하나 더해서 총 124개를 확인

    }
}