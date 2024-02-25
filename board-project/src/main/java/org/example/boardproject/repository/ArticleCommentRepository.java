package org.example.boardproject.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.example.boardproject.domain.ArticleComment;
import org.example.boardproject.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // yaml 파일에 설정을 annotated로 설정했기 때문에 붙여주어야 리파지터리로 인식 Spring Data REST 사용 준비 완료
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>, // 기본적으로 선택한 엔티티에 대한 검색기능을 추가
        QuerydslBinderCustomizer<QArticleComment> //
{
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) { // 디폴트 메서드를 사용
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.createdAt, root.createdBy); // 객체 필터를 추가
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // String이 아니기 때문에 사용하지만 조건에 완벽하게 부합하지는 않음
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }
}
