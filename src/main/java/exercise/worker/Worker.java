package exercise.worker;

import exercise.article.Article;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Worker {

    /*
    1. Подготавливает список статей (используя метод prepareArticles)
    2. Загружает их в библиотеку
    3. Обновляет каталог библиотеки если необходимо (если были отправлены новые статьи).
     */
    void addNewArticles(List<Article> articles);

    String getCatalog();

    List<Article> prepareArticles(List<Article> articles);
}
