package com.example.roomexbydt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomexbydt.Adapter.TasksAdapter;
import com.example.roomexbydt.AppDatbase.AppDatabase;
import com.example.roomexbydt.Model.Task;
import com.example.roomexbydt.TaskDao.TaskDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton buttonAddTask,buttonUp;
    private RecyclerView recyclerView;
    private EditText searchView;
    private Button mBtnSearch;
    private TasksAdapter mTaskAdapter;
    private LinearLayout mLLSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.user_search_bar);

        mBtnSearch = findViewById(R.id.btnSearch);
        mLLSearch = findViewById(R.id.LLSearch);

        recyclerView = findViewById(R.id.recyclerview_tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && buttonUp.getVisibility() == View.VISIBLE && buttonAddTask.getVisibility() == View.GONE ) {
                    buttonUp.hide();
                    buttonAddTask.show();
                                  }
                else if (dy < 0 && buttonUp.getVisibility() != View.VISIBLE && buttonAddTask.getVisibility() == View.VISIBLE) {
                    buttonUp.show();
                    buttonAddTask.hide();

                }
            }
        });


        buttonUp = findViewById(R.id.floating_button_up);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(0);
            }
        });





        buttonAddTask = findViewById(R.id.floating_button_add);



        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });


        getTasks();


        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"CLick On Search",Toast.LENGTH_LONG).show();
                getUser();
            }
        });

    }


    private void getTasks() {
        class GetTasks extends AsyncTask<Void, Void, List<Task>> {

            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getAll();
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                mTaskAdapter = new TasksAdapter(MainActivity.this, tasks);
                recyclerView.setAdapter(mTaskAdapter);
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
 private void getUser() {
        class GetUser extends AsyncTask<Void, Void, List<Task>> {

            String name= searchView.getText().toString();
            @Override
            protected List<Task> doInBackground(Void... voids) {
                List<Task> taskList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .taskDao()
                        .getTaskByFinishBy(name);
                return taskList;
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);
                mTaskAdapter= new TasksAdapter(MainActivity.this, tasks);
                recyclerView.setAdapter(mTaskAdapter);
                Toast.makeText(MainActivity.this, "Size"+tasks.size(), Toast.LENGTH_SHORT).show();
            }
        }

        GetUser getUser = new GetUser();
        getUser.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delelet_menu:




                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure delete all record?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAllTask();
                        Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
                return true;

            case R.id.home:
                getTasks();
                return true;


        }

        return super.onOptionsItemSelected(item);

    }


    private void deleteAllTask() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .taskDao()
                        .deleteAllTask();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }




}