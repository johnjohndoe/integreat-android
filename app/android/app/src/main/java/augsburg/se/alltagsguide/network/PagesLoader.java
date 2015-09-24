package augsburg.se.alltagsguide.network;

import android.app.Activity;

import com.google.inject.Inject;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import augsburg.se.alltagsguide.common.Language;
import augsburg.se.alltagsguide.common.Location;
import augsburg.se.alltagsguide.common.Page;
import augsburg.se.alltagsguide.persistence.DatabaseCache;
import augsburg.se.alltagsguide.persistence.resources.PageResource;
import augsburg.se.alltagsguide.utilities.BasicLoader;

/**
 * Created by Daniel-L on 07.09.2015.
 */
public class PagesLoader extends BasicLoader<List<Page>> {

    @Inject
    private DatabaseCache dbCache;
    private Location mLocation;
    private Language mLanguage;

    @Inject
    private PageResource.Factory pagesFactory;


    /**
     * Create loader for context
     *
     * @param activity
     */
    @Inject
    public PagesLoader(Activity activity, Location location, Language language) {
        super(activity);
        mLocation = location;
        mLanguage = language;
    }

    @Override
    public List<Page> load() {
        try {
            return dbCache.requestAndStore(pagesFactory.under(mLanguage, mLocation));
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}