package com.pw.parent.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [OfflineDraft::class, NotificationItem::class],
    version = 1,
    exportSchema = false
)
abstract class ParentAppDatabase : RoomDatabase() {
    abstract fun parentAppDao(): ParentAppDao

    companion object {
        @Volatile
        private var INSTANCE: ParentAppDatabase? = null

        fun getDatabase(context: Context): ParentAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ParentAppDatabase::class.java,
                    "parent_app_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
