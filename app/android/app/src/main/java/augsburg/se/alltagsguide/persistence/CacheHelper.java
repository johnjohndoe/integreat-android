package augsburg.se.alltagsguide.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.inject.Inject;

import augsburg.se.alltagsguide.utilities.PrefUtilities;
import roboguice.util.Ln;


/**
 * Based on https://github.com/pockethub/PocketHub/blob/master/app/src/main/java/com/github/pockethub/persistence/CacheHelper.java
 */
public class CacheHelper extends SQLiteOpenHelper {

    public static final String TABLE_PAGE = "pages";
    public static final String PAGE_ID = "p_id"; //1
    public static final String PAGE_TITLE = "p_title"; //2
    public static final String PAGE_TYPE = "p_type"; //3
    public static final String PAGE_STATUS = "p_status"; //4
    public static final String PAGE_MODIFIED = "p_modified"; //5
    public static final String PAGE_DESCRIPTION = "p_description"; //6
    public static final String PAGE_CONTENT = "p_content"; //7
    public static final String PAGE_PARENT_ID = "p_parent"; //8
    public static final String PAGE_ORDER = "p_order"; //9
    public static final String PAGE_THUMBNAIL = "p_thumbnail"; //10
    public static final String PAGE_LOCATION = "p_location"; //11
    public static final String PAGE_LANGUAGE = "p_language"; //12
    public static final String PAGE_AUTHOR = "p_author"; //13
    public static final int PAGE_OFFSET = 13; //TODO CHANGABLE

    public static final String TABLE_PAGE_AVAILABLE_LANGUAGE = "pages_languages";
    public static final String PAGE_AVAIL_PAGE_ID = "pa_id"; // 1
    public static final String PAGE_AVAIL_PAGE_LOCATION = "pa_location"; // 2
    public static final String PAGE_AVAIL_PAGE_LANGUAGE = "pa_language"; // 3
    public static final String PAGE_AVAIL_OTHER_LANGUAGE = "pa_other_lang"; // 4
    public static final String PAGE_AVAIL_OTHER_PAGE = "pa_other_page"; // 5

    public static final String TABLE_EVENT = "events";
    public static final String EVENT_ID = "e_id"; //1
    public static final String EVENT_START = "e_start"; //2
    public static final String EVENT_END = "e_end"; //3
    public static final String EVENT_ALL_DAY = "e_allday"; //4
    public static final String EVENT_LOCATION = "e_location"; //5
    public static final String EVENT_LANGUAGE = "e_language"; //6
    public static final String EVENT_PAGE = "e_page"; //7
    public static final String EVENT_PAGE_ID = "e_page_id"; //8

    public static final String TABLE_CATEGORY = "category";
    public static final String CATEGORY_ID = "c_id"; // 1
    public static final String CATEGORY_NAME = "c_name"; // 2
    public static final String CATEGORY_PARENT = "c_parent"; // 3

    public static final String TABLE_EVENT_CATEGORY = "event_category";
    public static final String EVENT_CATEGORY_ID = "ec_id"; // 1
    public static final String EVENT_CATEGORY_LOCATION = "ec_location"; // 2
    public static final String EVENT_CATEGORY_LANGUAGE = "ec_language"; // 3
    public static final String EVENT_CATEGORY_PAGE = "ec_page"; // 4
    public static final String EVENT_CATEGORY_EVENT_ID = "ec_event_id"; // 5

    public static final String TABLE_EVENT_TAG = "event_tags";
    public static final String EVENT_TAG_NAME = "et_name"; // 1
    public static final String EVENT_TAG_LOCATION = "et_location"; // 2
    public static final String EVENT_TAG_LANGUAGE = "et_language"; // 3
    public static final String EVENT_TAG_PAGE = "et_page"; // 4
    public static final String EVENT_TAG_EVENT_ID = "et_event_id"; // 5

    public static final String TABLE_TAG = "tags";
    public static final String TAG_NAME = "t_name"; // 1

    public static final String TABLE_AUTHOR = "authors";
    public static final String AUTHOR_USERNAME = "a_username"; // 1
    public static final String AUTHOR_FIRSTNAME = "a_firstname"; // 2
    public static final String AUTHOR_LASTNAME = "a_lastname"; //  3

    public static final String TABLE_EVENT_LOCATION = "event_location";
    public static final String EVENT_LOCATION_NAME = "el_location_name"; // 1
    public static final String EVENT_LOCATION_ADDRESS = "el_location_address"; // 2
    public static final String EVENT_LOCATION_TOWN = "el_location_town"; // 3
    public static final String EVENT_LOCATION_STATE = "el_location_state"; // 4
    public static final String EVENT_LOCATION_POSTCODE = "el_location_postcode"; // 5
    public static final String EVENT_LOCATION_REGION = "el_location_region"; // 6
    public static final String EVENT_LOCATION_COUNTRY = "el_location_country"; // 7
    public static final String EVENT_LOCATION_LATITUDE = "el_location_latitude"; // 8
    public static final String EVENT_LOCATION_LONGITUDE = "el_location_longitude"; // 9
    public static final String EVENT_LOCATION_PAGE = "el_location_page"; //10
    public static final String EVENT_LOCATION_LOCATION = "el_location"; //11
    public static final String EVENT_LOCATION_LANGUAGE = "el_language"; //12

    public static final String TABLE_LANGUAGE = "languages";
    public static final String LANGUAGE_ID = "l_id"; //1
    public static final String LANGUAGE_SHORT = "l_short"; //2
    public static final String LANGUAGE_NAME = "l_name"; //3
    public static final String LANGUAGE_PATH = "l_path"; //4
    public static final String LANGUAGE_LOCATION = "l_location"; //5

    public static final String TABLE_LOCATION = "locations";
    public static final String LOCATION_ID = "lo_id"; //1
    public static final String LOCATION_NAME = "lo_name"; //2
    public static final String LOCATION_ICON = "lo_icon"; //3
    public static final String LOCATION_PATH = "lo_path"; //4
    public static final String LOCATION_DESCRIPTION = "lo_description"; //5
    public static final String LOCATION_GLOBAL = "lo_global"; //6
    public static final String LOCATION_COLOR = "lo_color"; //7
    public static final String LOCATION_CITY_IMAGE = "lo_city_image"; //8
    public static final String LOCATION_LATITUDE = "lo_latitude"; //9
    public static final String LOCATION_LONGITUDE = "lo_longitude"; //10

    private PrefUtilities mPrefUtilities;

    @Inject
    public CacheHelper(final Context context, DatabaseInfo databaseInfo, PrefUtilities prefUtilities) {
        super(context, databaseInfo.getName(), null, databaseInfo.getVersion());
        mPrefUtilities = prefUtilities;
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        createLocationTable(db);
        createLanguageTable(db);

        createPageTable(db);
        createAuthorTable(db);
        createPageAvailableLanguagesTable(db);

        createEventTable(db);
        createEventLocationTable(db);

        createTagTable(db);
        createEventTagTable(db);

        createCategoryTable(db);
        createEventCategoryTable(db);
    }

    private void createPageAvailableLanguagesTable(SQLiteDatabase db) {
        String availableLanguagesQuery = "CREATE TABLE " + TABLE_PAGE_AVAILABLE_LANGUAGE + "(" +
                PAGE_AVAIL_PAGE_ID + " INTEGER," +
                PAGE_AVAIL_PAGE_LOCATION + " INTEGER," +
                PAGE_AVAIL_PAGE_LANGUAGE + " INTEGER," +
                PAGE_AVAIL_OTHER_LANGUAGE + " TEXT," +
                PAGE_AVAIL_OTHER_PAGE + " INTEGER" +
                ");";
        Ln.d(availableLanguagesQuery);
        db.execSQL(availableLanguagesQuery);
    }

    private void createCategoryTable(SQLiteDatabase db) {
        String categoryQuery = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                CATEGORY_ID + " INTEGER PRIMARY KEY," +
                CATEGORY_NAME + " TEXT," +
                CATEGORY_PARENT + " INTEGER" +
                ");";
        Ln.d(categoryQuery);
        db.execSQL(categoryQuery);
    }

    private void createTagTable(SQLiteDatabase db) {
        String tagQuery = "CREATE TABLE " + TABLE_TAG + "(" +
                TAG_NAME + " TEXT PRIMARY KEY" +
                ");";
        Ln.d(tagQuery);
        db.execSQL(tagQuery);
    }

    private void createEventCategoryTable(SQLiteDatabase db) {
        String categoryEventQuery = "CREATE TABLE " + TABLE_EVENT_CATEGORY + "(" +
                EVENT_CATEGORY_ID + " INTEGER," +
                EVENT_CATEGORY_LANGUAGE + " INTEGER," +
                EVENT_CATEGORY_LOCATION + " INTEGER," +
                EVENT_CATEGORY_PAGE + " INTEGER," +
                EVENT_CATEGORY_EVENT_ID + " INTEGER," +
                "PRIMARY KEY(" +
                EVENT_CATEGORY_ID + "," +
                EVENT_CATEGORY_LANGUAGE + "," +
                EVENT_CATEGORY_LOCATION + "," +
                EVENT_CATEGORY_PAGE + "," +
                EVENT_CATEGORY_EVENT_ID + ")" +
                ");";
        Ln.d(categoryEventQuery);
        db.execSQL(categoryEventQuery);
    }


    private void createEventTagTable(SQLiteDatabase db) {
        String tagEventQuery = "CREATE TABLE " + TABLE_EVENT_TAG + "(" +
                EVENT_TAG_NAME + " TEXT," +
                EVENT_TAG_LANGUAGE + " INTEGER," +
                EVENT_TAG_LOCATION + " INTEGER," +
                EVENT_TAG_PAGE + " INTEGER," +
                EVENT_TAG_EVENT_ID + " INTEGER," +
                "PRIMARY KEY(" +
                EVENT_TAG_NAME + "," +
                EVENT_TAG_LANGUAGE + "," +
                EVENT_TAG_LOCATION + "," +
                EVENT_TAG_PAGE + "," +
                EVENT_TAG_EVENT_ID + ")" +
                ");";
        Ln.d(tagEventQuery);
        db.execSQL(tagEventQuery);
    }

    private void createEventLocationTable(SQLiteDatabase db) {
        String eventLocationQuery = "CREATE TABLE " + TABLE_EVENT_LOCATION + "(" +
                EVENT_LOCATION_NAME + " TEXT," +
                EVENT_LOCATION_ADDRESS + " TEXT," +
                EVENT_LOCATION_TOWN + " TEXT," +
                EVENT_LOCATION_STATE + " TEXT," +
                EVENT_LOCATION_POSTCODE + " INTEGER," +
                EVENT_LOCATION_REGION + " TEXT," +
                EVENT_LOCATION_COUNTRY + " TEXT," +
                EVENT_LOCATION_LATITUDE + " FLOAT," +
                EVENT_LOCATION_LONGITUDE + " FLOAT," +
                EVENT_LOCATION_PAGE + " INTEGER," +
                EVENT_LOCATION_LOCATION + " INTEGER," +
                EVENT_LOCATION_LANGUAGE + " INTEGER," +
                "PRIMARY KEY(" + EVENT_LOCATION_PAGE + "," + EVENT_LOCATION_LOCATION + "," + EVENT_LOCATION_LANGUAGE + ")" +
                ");";
        Ln.d(eventLocationQuery);
        db.execSQL(eventLocationQuery);
    }

    private void createEventTable(SQLiteDatabase db) {
        String eventQuery = "CREATE TABLE " + TABLE_EVENT + "(" +
                EVENT_ID + " INTEGER," +
                EVENT_START + " FLOAT," +
                EVENT_END + " FLOAT," +
                EVENT_ALL_DAY + " INTEGER," +
                EVENT_LOCATION + " INTEGER," +
                EVENT_LANGUAGE + " INTEGER," +
                EVENT_PAGE + " INTEGER," +
                EVENT_PAGE_ID + " INTEGER," +
                "PRIMARY KEY(" + EVENT_ID + "," + EVENT_PAGE + "," + EVENT_LOCATION + "," + EVENT_LANGUAGE + ")" +
                ");";
        Ln.d(eventQuery);
        db.execSQL(eventQuery);
    }

    private void createAuthorTable(SQLiteDatabase db) {
        String authorQuery = "CREATE TABLE " + TABLE_AUTHOR + "(" +
                AUTHOR_USERNAME + " TEXT PRIMARY KEY," +
                AUTHOR_FIRSTNAME + " TEXT," +
                AUTHOR_LASTNAME + " TEXT" +
                ");";
        Ln.d(authorQuery);
        db.execSQL(authorQuery);
    }

    private void createLanguageTable(SQLiteDatabase db) {
        String languageQuery = "CREATE TABLE " + TABLE_LANGUAGE + "(" +
                LANGUAGE_ID + " INTEGER," +
                LANGUAGE_SHORT + " TEXT," +
                LANGUAGE_NAME + " TEXT," +
                LANGUAGE_PATH + " TEXT," +
                LANGUAGE_LOCATION + " INTEGER," +
                "PRIMARY KEY(" + LANGUAGE_ID + "," + LANGUAGE_LOCATION + ")" +
                ");";
        Ln.d(languageQuery);
        db.execSQL(languageQuery);
    }

    private void createLocationTable(SQLiteDatabase db) {
        String locationQuery = "CREATE TABLE " + TABLE_LOCATION + "(" +
                LOCATION_ID + " INTEGER PRIMARY KEY," +
                LOCATION_NAME + " TEXT," +
                LOCATION_ICON + " TEXT," +
                LOCATION_PATH + " TEXT," +
                LOCATION_DESCRIPTION + " TEXT," +
                LOCATION_GLOBAL + " INTEGER," +
                LOCATION_COLOR + " INTEGER," +
                LOCATION_CITY_IMAGE + " TEXT," +
                LOCATION_LATITUDE + " FLOAT," +
                LOCATION_LONGITUDE + " FLOAT" +
                ");";
        Ln.d(locationQuery);
        db.execSQL(locationQuery);
    }

    private void createPageTable(SQLiteDatabase db) {
        String pageQuery = "CREATE TABLE " + TABLE_PAGE + "(" +
                PAGE_ID + " INTEGER," +
                PAGE_TITLE + " TEXT," +
                PAGE_TYPE + " TEXT," +
                PAGE_STATUS + " TEXT," +
                PAGE_MODIFIED + " TEXT," +
                PAGE_DESCRIPTION + " TEXT," +
                PAGE_CONTENT + " TEXT," +
                PAGE_PARENT_ID + " INTEGER," +
                PAGE_ORDER + " INTEGER," +
                PAGE_THUMBNAIL + " TEXT," +
                PAGE_AUTHOR + " TEXT," +
                PAGE_LOCATION + " INTEGER," +
                PAGE_LANGUAGE + " INTEGER," +
                "PRIMARY KEY(" + PAGE_ID + "," + PAGE_LOCATION + "," + PAGE_LANGUAGE + ")" +
                ");";
        Ln.d(pageQuery);
        db.execSQL(pageQuery);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        resetPreferences();
        onCreate(db);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
                          final int newVersion) {
        dropAllTables(db);
        resetPreferences();
        onCreate(db);
    }

    private void resetPreferences() {
        mPrefUtilities.clear();
    }

    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LANGUAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHOR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGE_AVAILABLE_LANGUAGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_TAG);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_CATEGORY);
    }

}
