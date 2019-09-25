package com.rrooaarr.werkstueck;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WordDao extends BaseDao<Word> {

    @Insert
    // If the table has more than one column, you can use
    //@Insert(onConflict = OnConflictStrategy.REPLACE)

    void insert(Word word);

    @Query("DELETE FROM word_table")
    void deleteAll();

    /**You might want to use the @Transaction annotation for @Query methods that have a select statement, in the following cases:

    1) When the result of the query is fairly big. By querying the database in one transaction, you ensure that if the query result doesn’t fit in a single cursor window, it doesn’t get corrupted due to changes in the database between cursor window swaps.
    2) When the result of the query is a POJO with @Relation fields. The fields are queries separately so running them in a single transaction will guarantee consistent results between queries.**/
    @Query("SELECT * from word_table")
    LiveData<List<Word>> getAllWords();

    @Query("SELECT * from word_table ORDER BY word ASC")
    LiveData<List<Word>> getAlphabetizedWords();

    @Insert
    void insertAll(List<Word> words);

    @Transaction
    @Update
    public void updateData(List<Word> words);

}