package com.example.taazakhabar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface{

    //apikey=9678c10991fd454d878dbc81636e98e8

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModalArayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArayList,
                this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories(){
        categoryRVModalArayList.add(new CategoryRVModal("All","https://images.unsplash.com/photo-1580721658071-d69d71961fa5?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8YmxhY2slMjBza3l8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Technology", "https://images.unsplash.com/photo-1518770660439-4636190af475?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1628595351029-c2bf17511435?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTV8fFNwb3J0c3xlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1507925921958-8a62f3d1a50d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8R2VuZXJhbHxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1590283603385-17ffb3a7f29f?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nnx8c3RvY2slMjBleGNoYW5nZXxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1470229722913-7c0e2dbbafd3?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTR8fGVudGVydGFpbm1lbnR8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1584982751601-97dcc096659c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTF8fEhlYWx0aGNhcmV8ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60"));

        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+ category +"&apiKey=9678c10991fd454d878dbc81636e98e8";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9678c10991fd454d878dbc81636e98e8";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;

        if(category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }else{
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();
                for(int i=0; i< articles.size(); i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),
                            articles.get(i).getDescription(),articles.get(i).getUrlToImage()
                            ,articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this,"Failed to get News!!",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArayList.get(position).getCategory();
        getNews(category);
    }
}