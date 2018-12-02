package com.gc.materialdesigndemo.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * AsyncTask implementation that opens a network connection and
 * query's the Book Service API.
 */
public class DohvatiKnjige extends AsyncTask<String,Void,String> {

    // Variables for the search input field, and results TextViews
    private EditText mBookInput;
    private TextView mTitleText;
    private TextView mAuthorText;


    ArrayList<Knjiga> rez = new ArrayList<>();

    // Class name for Log tag
    private static final String LOG_TAG = DohvatiKnjige.class.getSimpleName();

    // Constructor providing a reference to the views in MainActivity

    public interface IDohvatiKnjigeDone​{
        public void onDohvatiDone(ArrayList<Knjiga> knjige);
    }

    private IDohvatiKnjigeDone​ pozivatelj;
    public DohvatiKnjige(IDohvatiKnjigeDone​ p) {pozivatelj = p;};


    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        String queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "5")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();

            URL requestURL = new URL(builtURI.toString());

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            // Initialize iterator and results fields.
            int i = 0;
            String title = null;
            String authors = null;
            String ID = null;
            String datum=null;
            String opis = null;
            String slika= null;
            int brojStranica = 0;

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.

            while (i < itemsArray.length()) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");
                JSONObject slikaInfo = volumeInfo.getJSONObject("imageLinks");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    ID = book.getString("id");
                    title = volumeInfo.getString("title");
                    opis = volumeInfo.getString("description");
                    datum = volumeInfo.getString("publishedDate");
                    brojStranica = volumeInfo.getInt("pageCount");
                    slika = slikaInfo.getString("thumbnail");
                    authors = volumeInfo.getString("authors");
                    JSONArray sviAutori = volumeInfo.getJSONArray("authors");
                    String autor = null;
                    ArrayList<String> autoriSvi=new ArrayList<String>();
                    for(int j=0;j<sviAutori.length();j++)
                    {
                        autor = sviAutori.getString(j);
                        autoriSvi.add(autor);
                    }
                    JSONArray sveKategorije = volumeInfo.getJSONArray("categories");
                    ArrayList<String> kategorije=new ArrayList<String>();
                    String kat = null;
                    for(int j=0;j<sveKategorije.length();j++)
                    {
                        kat = sveKategorije.getString(j);
                        kategorije.add(kat);
                    }

                    Knjiga knjiga = new Knjiga(ID, title, autoriSvi, opis, datum, slika, brojStranica, kategorije.get(0));
                    rez.add(knjiga);

                } catch (Exception e){
                    e.printStackTrace();
                }

                // Move to the next item.
                i++;
            }
            pozivatelj.onDohvatiDone(rez);


        } catch (Exception e){
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            e.printStackTrace();
        }
    }
}