package com.example.roomexbydt.AppDatbase;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.roomexbydt.Model.Task;
import com.example.roomexbydt.TaskDao.TaskDao;


@Database(entities = {Task.class}, version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDao taskDao();
}