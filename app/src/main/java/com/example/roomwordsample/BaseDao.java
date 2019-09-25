package com.example.roomwordsample;

import androidx.room.Insert;

interface BaseDao<T> {
    @Insert
    void insert(T obj);
}