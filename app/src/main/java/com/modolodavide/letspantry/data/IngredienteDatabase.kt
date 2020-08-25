package com.modolodavide.letspantry.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Ingrediente::class], version = 1, exportSchema = false)

abstract class IngredienteDatabase : RoomDatabase() {

    abstract fun ingredienteDAO(): IngredienteDAO

    companion object {
        @Volatile
        private var INSTANCE: IngredienteDatabase? = null

        fun getDatabase(context: Context): IngredienteDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        IngredienteDatabase::class.java,
                        "dispensa.db"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}