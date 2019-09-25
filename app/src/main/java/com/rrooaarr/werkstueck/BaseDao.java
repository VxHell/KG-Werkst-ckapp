package com.rrooaarr.werkstueck;

import androidx.room.Insert;

interface BaseDao<T> {
    @Insert
    void insert(T obj);
}