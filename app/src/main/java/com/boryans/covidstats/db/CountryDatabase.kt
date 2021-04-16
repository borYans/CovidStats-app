package com.boryans.covidstats.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.boryans.covidstats.model.Country

@Database(
    entities = [Country::class],
    version = 2,
    exportSchema = false
)
abstract class CountryDatabase:  RoomDatabase(){

    abstract fun getCountryDao(): CountryDao

    companion object{

        @Volatile
        private var instance: CountryDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instanceOfDatabase ->
                instance = instanceOfDatabase
            }
        }
        private fun createDatabase(context: Context) =
         Room.databaseBuilder(
             context.applicationContext,
             CountryDatabase::class.java,
             "country_db.db"
         ).build()






    }

}