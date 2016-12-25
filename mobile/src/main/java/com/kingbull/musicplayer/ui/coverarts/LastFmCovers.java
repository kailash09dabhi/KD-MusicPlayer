package com.kingbull.musicplayer.ui.coverarts;

import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Kailash Dabhi
 * @date 12/25/2016.
 */

public final class LastFmCovers implements CoverImages {

  @Override public Observable<List<String>> albums(final String album) {
    return Observable.defer(new Callable<ObservableSource<JSONObject>>() {
      @Override public ObservableSource<JSONObject> call() throws Exception {
        return Observable.just(requestWebService(String.format(
            "http://ws.audioscrobbler.com/2.0/?method=album"
                + ".search&api_key=2fbdb536c815a3788b8ec3a73069729b&artist=&album=%s&format=json",
            album)));
      }
    }).subscribeOn(Schedulers.io()).flatMap(new Function<JSONObject, Observable<List<String>>>() {
      @Override public Observable<List<String>> apply(JSONObject jsonObject) throws Exception {
        return Observable.just(parsedResponse(jsonObject));
      }
    });
  }

  @Override public Observable<List<String>> artists(final String artist) {
    return Observable.defer(new Callable<ObservableSource<JSONObject>>() {
      @Override public ObservableSource<JSONObject> call() throws Exception {
        return Observable.just(requestWebService(String.format("http://ws.audioscrobbler.com/2"
                + ".0/?method=artist"
                + ".search&api_key=2fbdb536c815a3788b8ec3a73069729b&artist=%s&album=&format=json",
            artist)));
      }
    }).subscribeOn(Schedulers.io()).flatMap(new Function<JSONObject, Observable<List<String>>>() {
      @Override public Observable<List<String>> apply(JSONObject jsonObject) throws Exception {
        return Observable.just(parsedResponse(jsonObject));
      }
    });
  }

  private String getResponseText(InputStream inStream) {
    // very nice trick from
    // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
    return new Scanner(inStream).useDelimiter("\\A").next();
  }

  private JSONObject requestWebService(String serviceUrl) {
    HttpURLConnection urlConnection = null;
    try {
      // create connection
      URL urlToRequest = new URL(serviceUrl);
      urlConnection = (HttpURLConnection) urlToRequest.openConnection();
      urlConnection.setConnectTimeout(1000);
      urlConnection.setReadTimeout(10000);
      // handle issues
      int statusCode = urlConnection.getResponseCode();
      if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
        // handle unauthorized (if service requires user login)
      } else if (statusCode != HttpURLConnection.HTTP_OK) {
        // handle any other errors, like 404, 500,..
      }
      // create JSON object from content
      InputStream in = new BufferedInputStream(urlConnection.getInputStream());
      return new JSONObject(getResponseText(in));
    } catch (MalformedURLException e) {
      // URL is invalid
    } catch (SocketTimeoutException e) {
      // data retrieval or connection timed out
    } catch (IOException e) {
      // could not read response body
      // (could not create input stream)
    } catch (JSONException e) {
      // response body is no valid JSON string
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
    return null;
  }

  private List<String> parsedResponse(JSONObject serviceResult) {
    List<String> imageUrls = new ArrayList<>();
    if (serviceResult != null) {
      try {
        serviceResult = serviceResult.getJSONObject("results").getJSONObject("albummatches");
        JSONArray jsonArray = serviceResult.getJSONArray("album");
        int size = jsonArray.length();
        for (int i = 0; i < size; i++) {
          JSONObject albumObject = jsonArray.getJSONObject(i);
          JSONArray images = albumObject.getJSONArray("image");
          String albumImageUrl = images.getJSONObject(images.length() - 1).optString("#text");
          if (!albumImageUrl.isEmpty()) imageUrls.add(albumImageUrl);
          Log.e("albumImageUrl", albumImageUrl);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return imageUrls;
  }
}
