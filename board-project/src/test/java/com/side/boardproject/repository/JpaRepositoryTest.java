package com.side.boardproject.repository;

import com.side.boardproject.config.JpaConfig;
import com.side.boardproject.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository,
                             @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select 테스트")
    @Test
    void givenTestData_whenSelecting_then(){
        //Given

        //When
        List<Article> articles = articleRepository.findAll();

        //Then
        assertThat(articles).isNotNull().hasSize(906);

        //@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
        //@DataJpaTest는 데이터 소스가 저렇게 되있어도 임베디드 디비를 쓴다.
        //따라서 @DataJpaTest 를 사용하는 테스트 클래스 위에 @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)를
        //붙여줘서 임베디드 디비로 대체하지 않고, 설정 파일에 적시된 데이터소스를 사용하게끔 했다.
    }

    @DisplayName("insert 테스트")
    @Test
    void givenTestData_whenInsert_then(){
        //Given
        long preCnt = articleRepository.count();

        //When
        Article savedArticle= articleRepository.save(Article.of("new article","new content","#hastag"));

        //Then
        assertThat(articleRepository.count()).isEqualTo(preCnt+1);
    }

    @DisplayName("update 테스트")
    @Test
    void givenTestData_whenUpdate_then(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springboot";
        article.setHashtag(updateHashtag);
        long prvCnt = articleRepository.count();

        //When
        Article savedArticle= articleRepository.saveAndFlush(article);

        //Then
        assertThat(article).hasFieldOrPropertyWithValue("hashtag",updateHashtag);
    }

    @DisplayName("delete 테스트")
    @Test
    void givenTestData_whenDelete_then(){
        //Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long prvCnt = articleRepository.count();
        long prvCmtCnt = articleCommentRepository.count();
        long deleteCmtSize = article.getArticleComments().size();

        //When
        articleRepository.delete(article);

        //Then
        assertThat(articleRepository.count()).isEqualTo(prvCnt-1);
        assertThat(articleCommentRepository.count()).isEqualTo(prvCmtCnt-deleteCmtSize);
    }

}