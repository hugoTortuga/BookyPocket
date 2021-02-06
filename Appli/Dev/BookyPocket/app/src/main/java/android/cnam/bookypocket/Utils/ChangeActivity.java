package android.cnam.bookypocket.Utils;

import android.cnam.bookypocket.Activities.BookDetailsActivity;
import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.content.Context;
import android.content.Intent;

import java.sql.SQLException;

public class ChangeActivity {

    public static void ChangeActivity(Context currentContext, Class wantedContext) {
        try {
            Intent intent = new Intent(currentContext, wantedContext);
            currentContext.startActivity(intent);
        } catch (Exception ex) {
            Alert.ShowDialog(currentContext, "Erreur lors du changement de page", "" + ex);
        }
    }

    public static void GoToBookDetailActivity(Context currentContext, Book book) throws SQLException {

        Intent intent = new Intent(currentContext, BookDetailsActivity.class);
        intent.putExtra("book", book);
        Author authorToSend = book.getAuthor();
        if (book.getAuthor() == null) {
            authorToSend = DataBaseSingleton.GetDataBaseSingleton(currentContext).getAuthorFromBook(book.getISBN());
        }
        if (book.getAuthor() != null) {
            if (!StringUtil.IsNullOrEmpty(authorToSend.getArtistName()))
                intent.putExtra("author", authorToSend.getArtistName());
        }
        currentContext.startActivity(intent);
    }

}
