package android.cnam.bookypocket.Utils;

import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckForm {

    public static Date CheckAndCastStringToDate(String date, Context context) throws ParseException {
        Date dateBirth = null;
        String regexDate = "\\d{4}/\\d{2}/\\d{2}";
        Pattern pattern = Pattern.compile(regexDate);
        Matcher matcher = pattern.matcher(date);
        if (!matcher.find()) {
            //Alert.ShowDialog(context, "Format invalide", "Veuillez remplir votre date de naissance au format AAAA/MM/JJ");
            return null;
        }
        date = date.replace('/', '-');
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateBirth = format.parse(date);
        return dateBirth;
    }

}
