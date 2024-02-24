package org.example.boardproject.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.example.boardproject.domain.Article;
import org.example.boardproject.domain.QArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>, // 기본적으로 선택한 엔티티에 대한 검색기능을 추가 그러나 단어 검색기능이 안됨
        QuerydslBinderCustomizer<QArticle> // 따라서 단어 검색기능이 되도록 만듬
{
    // QuerydslBinder에 customize 메서드를 오버라이드 함으로써 검색에 대한 세부적인 조정을 가능하게 만듬
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) { // 디폴트 메서드를 사용
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 객체 필터를 추가

//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase); // like '${v}' 쿼리생성문이 다름
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); // like '%${v}%' 부분 생성문이 가능
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // String이 아니기 때문에 사용하지만 조건에 완벽하게 부합하지는 않음
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);

    }

}