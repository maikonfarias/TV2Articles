package com.maikonfarias.tv2articles;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.maikonfarias.tv2articles.model.ArticleItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
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


public class ArticleListActivity extends ActionBarActivity {

    private ArrayList<ArticleItem> articleList = null;
    private ProgressBar progressbar = null;
    private ListView feedListView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        String url = "http://app-backend.tv2.dk/articles/v1/?section_identifier=2";
        //String url = "http://javatechig.com/api/get_category_posts/?dev=1&slug=android";
        new DownloadFilesTask().execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_article_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void updateList() {
        feedListView= (ListView) findViewById(R.id.custom_list);
        feedListView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.GONE);

        feedListView.setAdapter(new CustomListAdapter(this, articleList));
        feedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position,	long id) {
                /*Object o = feedListView.getItemAtPosition(position);
                ArticleItem newsData = (ArticleItem) o;

                Intent intent = new Intent(ArticleListActivity.this, ArticleListActivity.class);
                intent.putExtra("feed", newsData);
                startActivity(intent);*/
                Object o = feedListView.getItemAtPosition(position);
                ArticleItem articleData = (ArticleItem) o;
                Intent intent = new Intent(ArticleListActivity.this, WebViewActivity.class);
                intent.putExtra("url", articleData.getUrl());
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
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
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

    public void parseJson(JSONArray json) {
        try {

            // parsing json object
            //if (json.getString("status").equalsIgnoreCase("ok")) {

                //JSONArray posts = json.getJSONArray("posts");
                JSONArray posts = (JSONArray)json;

                articleList = new ArrayList<ArticleItem>();

                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = (JSONObject) posts.getJSONObject(i);
                    ArticleItem item = new ArticleItem();
                    item.setTitle(post.getString("title"));
                    //item.setDate(post.getString("date"));
                    //item.setId(post.getString("id"));
                    item.setUrl(post.getString("url"));
                    //item.setContent(post.getString("content"));
                    item.setSmallTeaserImage(post.getString("small_teaser_image"));

                    //JSONArray attachments = post.getJSONArray("attachments");
                    /*if (null != attachments && attachments.length() > 0) {
                        JSONObject attachment = attachments.getJSONObject(0);
                        if (attachment != null)
                            item.setAttachmentUrl(attachment.getString("url"));
                    }*/

                    articleList.add(item);
                }
            //}
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
