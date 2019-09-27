package com.rrooaarr.werkstueck;

import androidx.room.Insert;

public interface BaseDao<T> {
    @Insert
    long insert(T obj);
}