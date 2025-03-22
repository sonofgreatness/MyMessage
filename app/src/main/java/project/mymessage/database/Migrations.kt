package project.mymessage.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migrations {
    companion object{

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            // Step 1: Create a new table with the correct schema
            database.execSQL(
                """
            CREATE TABLE message_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                from_id TEXT NOT NULL, 
                to_id TEXT, 
                content TEXT NOT NULL DEFAULT 'default_content', 
                message_type INTEGER NOT NULL, 
                date_created INTEGER NOT NULL, 
                status TEXT NOT NULL, 
                is_read INTEGER NOT NULL CHECK (is_read IN (0,1)),
                FOREIGN KEY(from_id) REFERENCES conversation('from') ON DELETE CASCADE
            )
            """.trimIndent()
            )
            database.execSQL(
                """
            INSERT INTO message_new (id, from_id, to_id, content, message_type, date_created, status, is_read)
            SELECT id, from_id, to_id, COALESCE(content, 'default_content'), message_type, date_created, status, is_read FROM message
            """.trimIndent()
            )
            database.execSQL("DROP TABLE message")
            database.execSQL("ALTER TABLE message_new RENAME TO message")
            database.execSQL("CREATE INDEX index_message_from_id ON message (from_id)")
        }
        }

    }
}