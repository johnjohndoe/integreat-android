package augsburg.se.alltagsguide.start;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import augsburg.se.alltagsguide.R;
import augsburg.se.alltagsguide.common.Language;
import augsburg.se.alltagsguide.common.Location;
import augsburg.se.alltagsguide.network.LanguageLoader;
import augsburg.se.alltagsguide.utilities.BaseFragment;
import augsburg.se.alltagsguide.utilities.EmptyRecyclerView;
import roboguice.inject.InjectView;


public class LanguageFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Language>>, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_LOCATION = "location";
    private Location mLocation;
    private OnLanguageFragmentInteractionListener mListener;
    private LanguageAdapter mAdapter;

    @InjectView(R.id.swipe_refresh)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @InjectView(R.id.recycler_view)
    private EmptyRecyclerView mRecyclerView;

    @InjectView(R.id.emptyView)
    private View mEmptyView;

    @InjectView(R.id.city_name)
    private TextView cityTextView;

    public static LanguageFragment newInstance(Location location) {
        LanguageFragment fragment = new LanguageFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public LanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = (Location) getArguments().getSerializable(ARG_LOCATION);
        } else {
            throw new IllegalStateException("Location should not be null");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle("SELECT A LANGUAGE");
        setSubTitle("What language do you speak?");
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));
        mAdapter = new LanguageAdapter(new LanguageAdapter.LanguageClickListener() {
            @Override
            public void onLanguageClick(Language language) {
                mListener.onLanguageSelected(mLocation, language);
            }
        }, getActivity());
        mRecyclerView.setAdapter(mAdapter);
        cityTextView.setText(mLocation.getName());
        mRecyclerView.setEmptyView(mEmptyView);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLanguageFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnLanguageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public Loader<List<Language>> onCreateLoader(int i, Bundle bundle) {
        Loader<List<Language>> loader = new LanguageLoader(getActivity(), mLocation);
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Language>> loader, List<Language> languages) {
        mAdapter.add(languages);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onLoaderReset(Loader<List<Language>> loader) {
    }

    @Override
    public void onRefresh() {
        getLoaderManager().initLoader(0, null, this);
    }

    public interface OnLanguageFragmentInteractionListener {
        void onLanguageSelected(Location location, Language language);
    }

}
