package com.example.mobilt_java24_anton_smedberg_apl_intergration_v5.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 3, // bumpad till 3
    entities = [CityEntity::class, WeatherCacheEntity::class, RecentCityEntity::class],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun weatherDao(): WeatherDao
    abstract fun recentDao(): RecentCityDao

    companion object {

        // 1 → 2: skapa recent_cities + deduplicera cities och skapa unikt index
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // recent_cities
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS recent_cities (
                        name TEXT NOT NULL,
                        country TEXT,
                        admin1 TEXT,
                        latitude REAL NOT NULL,
                        longitude REAL NOT NULL,
                        lastVisited INTEGER NOT NULL,
                        PRIMARY KEY(name, latitude, longitude)
                    )
                    """.trimIndent()
                )

                // bygg om cities utan dubbletter
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS cities_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        country TEXT,
                        admin1 TEXT,
                        latitude REAL NOT NULL,
                        longitude REAL NOT NULL
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO cities_new (id, name, country, admin1, latitude, longitude)
                    SELECT MIN(id), name, country, admin1, latitude, longitude
                    FROM cities
                    GROUP BY name, latitude, longitude
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE cities")
                db.execSQL("ALTER TABLE cities_new RENAME TO cities")

                // Skapa index med det NAMN Room förväntar sig:
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_cities_name_latitude_longitude
                    ON cities(name, latitude, longitude)
                    """.trimIndent()
                )
            }
        }

        // 2 → 3: fixa felaktigt index-namn på enheter som redan ligger på v2
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Droppa ev. gammalt namn
                db.execSQL("DROP INDEX IF EXISTS idx_cities_name_lat_lon")
                // Skapa korrekt namn
                db.execSQL(
                    """
                    CREATE UNIQUE INDEX IF NOT EXISTS index_cities_name_latitude_longitude
                    ON cities(name, latitude, longitude)
                    """.trimIndent()
                )
            }
        }

        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "app-db")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                // Under utveckling kan du lätta på trycket:
                // .fallbackToDestructiveMigration()
                .build()
    }
}