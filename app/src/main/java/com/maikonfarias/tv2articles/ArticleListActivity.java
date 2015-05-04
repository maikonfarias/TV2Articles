package com.maikonfarias.tv2articles;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maikonfarias.tv2articles.model.ArticleItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ArticleListActivity extends AppCompatActivity {

    private final String articlesUrl = "http://app-backend.tv2.dk/articles/v1/?section_identifier=2";
    private ArrayList<ArticleItem> articleList = null;
    private ProgressBar progressbar = null;
    private ListView feedListView = null;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        getArticles();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getArticles();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    public void getArticles() {
        new DownloadFilesTask().execute(articlesUrl);
    }

    public void updateList() {
        feedListView = (ListView)findViewById(R.id.custom_list);
        feedListView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);

        feedListView.setAdapter(new CustomListAdapter(this, articleList));
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = feedListView.getItemAtPosition(position);
                ArticleItem articleData = (ArticleItem) o;
                Intent intent = new Intent(ArticleListActivity.this, WebViewActivity.class);
                intent.putExtra("url", articleData.getUrl());
                intent.putExtra("public_url", articleData.getPublicUrl());
                intent.putExtra("title", articleData.getTitle());
                startActivity(intent);
            }
        });
    }

    private class DownloadFilesTask extends AsyncTask<String, Integer, Void> {

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(Void result) {
            if (null != articleList) {
                updateList();
            }
        }

        @Override
        protected Void doInBackground(String... params) {
            String url = params[0];

            // getting JSON string from URL
            JSONArray json = getJSONFromUrl(url);

            //parsing json data
            parseJson(json);
            return null;
        }
    }


    public JSONArray getJSONFromUrl(String url) {
        InputStream is = null;
        JSONArray jArr = null;
        String json = null;

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jArr = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jArr;

    }

    public void parseJson(JSONArray posts) {
        try {
            articleList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.getJSONObject(i);
                ArticleItem item = new ArticleItem();
                item.setTitle(post.getString("title"));
                item.setIdentifier(post.getString("identifier"));
                item.setUrl(post.getString("url"));
                item.setSmallTeaserImage(post.getString("small_teaser_image"));
                item.setTeaserImage(post.getString("teaser_image"));
                item.setCategory(post.getString("category"));
                item.setHasVideo(post.getString("has_video"));
                item.setIsBreaking(post.getString("is_breaking"));
                item.setIsExternal(post.getString("is_external"));
                item.setIsLive(post.getString("is_live"));
                item.setModified(post.getString("modified"));
                item.setPublicUrl(post.getString("public_url"));

                articleList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                getArticles();
                mSwipeRefreshLayout.setRefreshing(true);
                break;
            default:
                break;
        }
        return true;
    }
}
