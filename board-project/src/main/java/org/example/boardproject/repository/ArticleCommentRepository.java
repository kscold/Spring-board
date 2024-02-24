package org.example.boardproject.repository;

import org.example.boardproject.domain.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // yaml 파일에 설정을 annotated로 설정했기 때문에 붙여주어야 리파지터리로 인식 Spring Data REST 사용 준비 완료
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {

}
