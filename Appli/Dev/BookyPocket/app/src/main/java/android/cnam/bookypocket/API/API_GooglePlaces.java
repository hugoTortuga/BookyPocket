package android.cnam.bookypocket.API;

import android.cnam.bookypocket.DBManager.DataBaseSingleton;
import android.cnam.bookypocket.Model.Author;
import android.cnam.bookypocket.Model.Book;
import android.cnam.bookypocket.Model.Category;
import android.cnam.bookypocket.Model.Library;
import android.cnam.bookypocket.Model.Photo;
import android.cnam.bookypocket.Utils.StringUtil;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class API_GooglePlaces {

    private static final String API_KEY = "AIzaSyANwoe5yvq5KocIFHBDlngGeNSFsTp5pnI";
    private static final int Radius = 1000;

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static String ReadStringJSON(String url) throws IOException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            return jsonText;
        } finally {
            is.close();
        }
    }

    private static JSONObject ReadJsonFromUrl(String url) throws IOException, JSONException {
        return new JSONObject(ReadStringJSON(url));
    }

    public static List<Library> Request(double longi, double lati, Context context) throws Exception {

        List<Library> libraries = new ArrayList<>();
        longi = 49.26030579;
        lati = 4.04678455666116;

        final String URL_Place_Id = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=library&location=" + longi +"," + lati +"&key=" + API_KEY + "&radius=" + Radius;
        JSONObject json = API_GooglePlaces.ReadJsonFromUrl(URL_Place_Id);


        List<String> listPlaceId = getPlaceIdFromJSONObject(json);

        for (String placeId: listPlaceId ) {
            String Libraries_Details = "https://maps.googleapis.com/maps/api/place/details/json?place_id="+ placeId + "&key=" + API_KEY;
            JSONObject jsonPlaceID = API_GooglePlaces.ReadJsonFromUrl(Libraries_Details);
            libraries.add(getLibraryFromPlaceId(jsonPlaceID));
        }
        return libraries;
    }

    private static List<String> getPlaceIdFromJSONObject(JSONObject json) throws JSONException {
        List<String> places_id = new ArrayList<>();
        JSONArray array = json.getJSONArray("predictions");
        for(int i = 0; i < array.length(); i++){
            JSONObject item = array.getJSONObject(i);
            String placeID = item.getString("place_id");
            places_id.add(placeID);
        }
        return places_id;
    }

    private static Library getLibraryFromPlaceId(JSONObject jsonPlaceID) throws JSONException {
        JSONObject item = jsonPlaceID.getJSONObject("result");
        JSONObject geometry = item.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        double lat = location.getDouble("lat");
        double lng = location.getDouble("lng");
        Library l = new Library();
        l.setLat(lat);
        l.setLong(lng);
        return l;
    }



}
