package exercise.worker;

import exercise.article.Article;
import exercise.article.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkerImplTest {

    private WorkerImpl worker;
    private Library mockLibrary;

    @BeforeEach
    void setUp() {
        mockLibrary = mock(Library.class);
        worker = new WorkerImpl(mockLibrary);
    }

    @Test
    void callingMethodWithEmptyListOfArticles() {
        worker.addNewArticles(new ArrayList<>());
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }

    @Test
    void reAddedArticleToLibrary() {
        List<Article> articles = new ArrayList<>();
        Article existingArticle = new Article("Existing Title", "Content", "Author", LocalDate.of(2024, 1, 1));
        articles.add(existingArticle);
        when(mockLibrary.getAllTitles()).thenReturn(List.of("Existing Title"));
        worker.addNewArticles(articles);
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }

       @Test
    void settinTheCurrentDateIfTheDateIsNotSet() {
        List<Article> articles = new ArrayList<>();
        Article article = new Article("Title", "Content", "Author", null);
        articles.add(article);
        List<Article> preparedArticles = worker.prepareArticles(articles);
        assertNotNull(preparedArticles.get(0).getCreationDate());
    }

    @Test
    void cannotReAddAnArticleToTheLibrary() {
        List<Article> articles = new ArrayList<>();
        Article existingArticle = new Article("Existing Title", "Content", "Author", LocalDate.of(2024, 1, 1));
        articles.add(existingArticle);

        when(mockLibrary.getAllTitles()).thenReturn(List.of("Existing Title"));
        worker.addNewArticles(articles);
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }


    @Test
    public void anArticleWithAnEmptyTitleNotAddToTheList() {

        WorkerImpl worker = new WorkerImpl(mockLibrary);
        Article article = new Article("", "Содержание", "Автор", LocalDate.now());
        List<Article> preparedArticles = worker.prepareArticles(List.of(article));
        assertTrue(preparedArticles.isEmpty());
    }

    @Test
    public void anArticleWithEmptyContentNotAddToTheList() {
       WorkerImpl worker = new WorkerImpl(mockLibrary);
       Article article = new Article("Заголовок", "", "Автор", LocalDate.now());
       List<Article> preparedArticles = worker.prepareArticles(List.of(article));
       assertTrue(preparedArticles.isEmpty());
    }

    @Test
    public void anArticleWithoutAnAuthorNotAddInTheList() {
       WorkerImpl worker = new WorkerImpl(mockLibrary);
       Article article = new Article("Заголовок", "Содержание", "", LocalDate.now());
       List<Article> preparedArticles = worker.prepareArticles(List.of(article));
       assertTrue(preparedArticles.isEmpty());
    }

    @Test
    public void ArticlesSetsCurrentDate() {
        WorkerImpl worker = new WorkerImpl(mockLibrary);
        Article article = new Article("Заголовок", "Содержание", "Автор", null);
        List<Article> preparedArticles = worker.prepareArticles(List.of(article));
        LocalDate currentDate = LocalDate.now();
        LocalDate articleDate = preparedArticles.get(0).getCreationDate();
        assertEquals(currentDate, articleDate);
    }
}