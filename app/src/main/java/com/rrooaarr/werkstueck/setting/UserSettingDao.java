package com.rrooaarr.werkstueck.setting;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.rrooaarr.werkstueck.BaseDao;

import java.util.List;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserSettingDao extends BaseDao<UserSetting> {

    @Insert(onConflict = IGNORE)
    long insert(UserSetting setting);

    @Query("DELETE FROM user_setting")
    void deleteAll();

    /**You might want to use the @Transaction annotation for @Query methods that have a select statement, in the following cases:

    1) When the result of the query is fairly big. By querying the database in one transaction, you ensure that if the query result doesn’t fit in a single cursor window, it doesn’t get corrupted due to changes in the database between cursor window swaps.
    2) When the result of the query is a POJO with @Relation fields. The fields are queries separately so running them in a single transaction will guarantee consistent results between queries.**/
    @Query("SELECT * from user_setting")
    LiveData<UserSetting> getSetting();

    @Query("SELECT * from user_setting ORDER BY username ASC")
    LiveData<List<UserSetting>> getAlphabetizedSettings();

    @Query("SELECT * from user_setting")
    List<UserSetting> getAllSettings2();

    @Update(onConflict = REPLACE)
    void update(UserSetting setting);

    @Query("UPDATE user_setting SET server = :server, port = :port,username = :username, password = :password WHERE id = :usid")
    int updateProperties(long usid, String server,String port, String username, String password);

}