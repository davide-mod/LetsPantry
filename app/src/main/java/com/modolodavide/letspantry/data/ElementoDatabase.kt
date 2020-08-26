package com.modolodavide.letspantry.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Elemento::class], version = 1, exportSchema = false)

abstract class ElementoDatabase : RoomDatabase() {

    abstract fun elementoDao(): ElementoDAO

    companion object {
        @Volatile
        private var INSTANCE: ElementoDatabase? = null

        fun getDatabase(context: Context): ElementoDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        ElementoDatabase::class.java,
                        "listaspesa.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}