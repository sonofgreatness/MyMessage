package project.mymessage.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import project.mymessage.database.Daos.ConversationDao
import project.mymessage.database.Daos.MessageDao
import project.mymessage.database.Daos.SearchDao
import project.mymessage.database.Entities.Conversation
import project.mymessage.database.Entities.Message
import project.mymessage.database.Entities.SearchQuery
import project.mymessage.database.typeConverters.TimestampConverter
import project.mymessage.database.typeConverters.UUIDConverter
import project.mymessage.util.Constants

@Database
    (
    entities = [Message::class,Conversation::class,
    SearchQuery::class
    ], version = 3,
    exportSchema = false
)
@TypeConverters(TimestampConverter::class, UUIDConverter::class)
abstract class MainDatabase :RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
    abstract fun getConversationDao(): ConversationDao
    abstract fun getSearchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getDatabase(context: Context): MainDatabase {
            Log.d("Database", "Database instance created")
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    Constants.LOCAL_DATABASE_NAME

                )
                    .addMigrations(Migrations.MIGRATION_2_3)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                return instance
            }
        }

    }



}