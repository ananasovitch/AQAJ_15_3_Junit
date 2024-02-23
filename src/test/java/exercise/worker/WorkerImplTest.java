package exercise.worker;

import exercise.article.Article;
import exercise.article.Library;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkerImplTest {

    @Mock
    private Library mockLibrary;
    private WorkerImpl worker;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        worker = new WorkerImpl(mockLibrary);
    }

    @Test
    @DisplayName("При вызове метода addNewArticles с пустым списком статей ни одна статья не сохраняется в библиотеке")
    public void callingMethodWithEmptyListOfArticles() {
        worker.addNewArticles(new ArrayList<>());
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }

    @Test
    @DisplayName("Повторное добавление статьи в библиотеку не приводит к ее дублированию")
    public void reAddedArticleToLibrary() {
        List<Article> articles = new ArrayList<>();
        Article existingArticle = new Article("Existing Title", "Content", "Author", LocalDate.of(2024, 1, 1));
        articles.add(existingArticle);
        when(mockLibrary.getAllTitles()).thenReturn(List.of("Existing Title"));
        worker.addNewArticles(articles);
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }

    @Test
    @DisplayName("Если у статьи не указана дата создания, то метод prepareArticles устанавливает текущую дату")
    public void settinTheCurrentDateIfTheDateIsNotSet() {
        List<Article> articles = new ArrayList<>();
        Article article = new Article("Title", "Content", "Author", null);
        articles.add(article);
        List<Article> preparedArticles = worker.prepareArticles(articles);
        assertNotNull(preparedArticles.get(0).getCreationDate());
    }

    @Test
    @DisplayName("Нельзя повторно добавить в библиотеку уже существующую статью")
    public void cannotReAddAnArticleToTheLibrary() {
        List<Article> articles = new ArrayList<>();
        Article existingArticle = new Article("Existing Title", "Content", "Author", LocalDate.of(2024, 1, 1));
        articles.add(existingArticle);

        when(mockLibrary.getAllTitles()).thenReturn(List.of("Existing Title"));
        worker.addNewArticles(articles);
        verify(mockLibrary, never()).store(anyInt(), anyList());
    }

    @Test
    @DisplayName("Статья с пустым заголовком не добавляется в список статей для обработки")
    public void anArticleWithAnEmptyTitleNotAddToTheList() {
        Article article = new Article("", "Содержание", "Автор", LocalDate.now());
        List<Article> preparedArticles = worker.prepareArticles(List.of(article));
        assertTrue(preparedArticles.isEmpty());
    }

    @Test
    @DisplayName("Статья с пустым содержанием не добавляется в список статей для обработки")
    public void anArticleWithEmptyContentNotAddToTheList() {
        Article article = new Article("Заголовок", "", "Автор", LocalDate.now());
        List<Article> preparedArticles = worker.prepareArticles(List.of(article));
        assertTrue(preparedArticles.isEmpty());
    }

    @Test
    @DisplayName(" статья без указания автора не добавляется в список статей для обработки")
    public void anArticleWithoutAnAuthorNotAddInTheList() {
        Article article = new Article("Заголовок", "Содержание", "", LocalDate.now());
        List<Article> preparedArticles = worker.prepareArticles(List.of(article));
        assertTrue(preparedArticles.isEmpty());
    }

}