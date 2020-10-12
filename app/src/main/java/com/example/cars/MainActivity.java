package com.example.cars;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.example.cars.api.MainInterface;
import com.example.cars.model.CarsList;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    List<CarsList> dataArrayList = new ArrayList<>();
    MainAdapter adapter;
    int page = 1, limit = 28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variables
        nestedScrollView = findViewById(R.id.scroll_view);
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progres_bar);

        //Initialize adapter
        adapter = new MainAdapter(MainActivity.this, dataArrayList);
        //Set layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set adapter
        recyclerView.setAdapter(adapter);
        //Create get data method
        getData(page, limit);

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //check condition
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    //when reach last item position
                    //increase page size
                    page++;
                    //show progress bar
                    progressBar.setVisibility(View.VISIBLE);
                    //call method
                    getData(page, limit);
                }
            }
        });
    }

    private void getData(int page, int limit) {
        //initialize retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://development.espark.lt/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //Create main interface
        MainInterface mainInterface = retrofit.create(MainInterface.class);
        //Initialize class
        Call<List<CarsList>> call = mainInterface.getCarsList(page, limit);

        call.enqueue(new Callback<List<CarsList>>() {
            @Override
            public void onResponse(@NotNull Call<List<CarsList>> call, @NotNull Response<List<CarsList>> response) {
                //Check condition
                if (response.isSuccessful() && response.body() != null) {
                    //hide progress bar
                    progressBar.setVisibility(View.GONE);
                    dataArrayList = response.body();
                    adapter = new MainAdapter(MainActivity.this, dataArrayList);
                    //Set adapter
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(@NotNull Call<List<CarsList>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //call filter
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
