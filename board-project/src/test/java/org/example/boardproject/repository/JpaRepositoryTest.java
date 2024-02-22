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


//@ActiveProfiles("testdb") // MySQL 같은 것과 연동을 좋게 만들려면 인메모리 db 주소를 고정으로 바꿈
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 이 어노테이션을 통해 TestDB를 안쓴다고 명시 기본값은 ANY
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class) // 오디팅을 사용중이기 때문에 인식할 수 있도록 설정 클래스를 임포트해줌
@DataJpaTest // 모든 db 설정을 무시하고 TestDB 설정으로 만들어버림
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


        // When
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);// 123개d의 데이터를 잘 불러 오는지 확인

    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_wheninserting_thenWorksFine() {
        // Given
        long previousCount = articleRepository.count(); // 총 테스트 데이터의 갯수 반환

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#spring")); // 하나의 임의의 데이터를 넣음

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1); // 하나 더해서 총 124개를 확인

    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // 첫번째 아이디 데이터를 가져오고 없으면 에러를 던짐
        String updatedHashtag = "#springboot"; // 해쉬태그를 업데이트
        article.setHashtag(updatedHashtag); // 새터로 값을 설정

        // When
        Article savedArticle = articleRepository.saveAndFlush(article);
        // 업데이트도 똑같이 save 메서드를 사용 그러나 업데이트의 경우 data를 다시 롤백하기 떄문에 saveAndFlush 메서드를 사용


        // Then
        assertThat(savedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag); // hasFieldOrPropertyWithValue 하나의 객체의 프로퍼티를 검사하는데 효과적임

    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow(); // 1번 아이디 객체를 찾음
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentSize = article.getArticleComments().size(); // 연관매핑 OneToMany 객체의 사이즈 이므로 int

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1); // 글은 하나가 삭제
        assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentSize);
        // 댓글은 댓글 총 갯수에서 글에 마운트 있는 객체 사이즈 한 개를 뺌

    }
}