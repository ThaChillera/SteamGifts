package net.mabako.steam.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.mabako.steam.store.data.Picture;
import net.mabako.steam.store.data.Text;
import net.mabako.steam.store.viewholder.PictureViewHolder;
import net.mabako.steam.store.viewholder.TextViewHolder;
import net.mabako.steamgifts.activities.DetailActivity;
import net.mabako.steamgifts.adapters.IEndlessAdaptable;
import net.mabako.steamgifts.adapters.viewholder.GameViewHolder;
import net.mabako.steamgifts.core.R;
import net.mabako.steamgifts.data.Game;

import java.util.ArrayList;
import java.util.List;

public abstract class StoreFragment extends Fragment {
    protected boolean loaded;

    private LoadStoreTask task;

    protected Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_simple_list, container, false);

        setHasOptionsMenu(true);

        RecyclerView listView = (RecyclerView) layout.findViewById(R.id.list);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

        return layout;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (task != null)
            task.cancel(true);

        loaded = false;
    }

    public abstract LoadStoreTask getTaskToStart();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !loaded) {
            loaded = true;

            task = getTaskToStart();
            task.execute();
        }
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<IEndlessAdaptable> items = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.endless_spacer, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            } else {
                View view = LayoutInflater.from(getContext()).inflate(viewType, parent, false);
                if (viewType == Game.VIEW_LAYOUT) {
                    return new GameViewHolder(view, StoreFragment.this);
                } else if (viewType == Picture.VIEW_LAYOUT) {
                    return new PictureViewHolder(view, getContext());
                } else if (viewType == Text.VIEW_LAYOUT) {
                    return new TextViewHolder(view, getContext());
                }

                throw new IllegalStateException("No View");
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof GameViewHolder) {
                ((GameViewHolder) holder).setFrom((Game) getItem(position));
            } else if (holder instanceof PictureViewHolder) {
                ((PictureViewHolder) holder).setFrom((Picture) getItem(position));
            } else if (holder instanceof TextViewHolder) {
                ((TextViewHolder) holder).setFrom((Text) getItem(position));
            }
        }

        public IEndlessAdaptable getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position) == null ? 0 : items.get(position).getLayout();
        }

        public void add(IEndlessAdaptable adaptable) {
            items.add(adaptable);
            notifyItemInserted(items.size() - 1);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        ((DetailActivity) getActivity()).getCurrentFragment().onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return ((DetailActivity) getActivity()).getCurrentFragment().onOptionsItemSelected(item);
    }
}
