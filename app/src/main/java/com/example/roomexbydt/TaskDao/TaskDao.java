package com.example.roomexbydt.TaskDao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.roomexbydt.Model.Task;

import java.util.List;

@Dao
public abstract class TaskDao {

    @Query("SELECT * FROM task")
    abstract public List<Task> getAll();

    @Insert
    abstract public void insert(Task task);

    @Delete
    abstract public void delete(Task task);


    @Update
    abstract public void update(Task task);

    @Query("DELETE FROM task")
    abstract public int deleteAllTask();

    @Query("SELECT * FROM task WHERE finish_by LIKE :key")
    abstract public List<Task> getTaskByFinishBy(String key);


}
