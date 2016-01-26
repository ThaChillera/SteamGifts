package net.mabako.steamgifts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.mabako.steamgifts.adapters.viewholder.CommentViewHolder;
import net.mabako.steamgifts.adapters.viewholder.MessageHeaderViewHolder;
import net.mabako.steamgifts.data.Comment;
import net.mabako.steamgifts.data.MessageHeader;
import net.mabako.steamgifts.fragments.profile.MessageListFragment;

import java.util.List;

public class MessageAdapter extends EndlessAdapter {
    private static final long serialVersionUID = 5997941227817634662L;

    private MessageListFragment fragment;

    public void setFragmentValues(MessageListFragment fragment) {
        setLoadListener(fragment);
        this.fragment = fragment;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateActualViewHolder(View view, int viewType) {
        if (fragment == null)
            throw new IllegalStateException("no fragment");

        if (viewType == Comment.VIEW_LAYOUT) {
            return new CommentViewHolder(view, fragment.getActivity(), fragment);
        } else if (viewType == MessageHeader.VIEW_LAYOUT) {
            return new MessageHeaderViewHolder(view);
        }

        throw new IllegalStateException();
    }

    @Override
    protected void onBindActualViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageHeaderViewHolder) {
            ((MessageHeaderViewHolder) holder).setFrom((MessageHeader) getItem(position));
        } else if (holder instanceof CommentViewHolder) {
            ((CommentViewHolder) holder).setFrom((Comment) getItem(position));
        }
    }

    @Override
    protected boolean hasEnoughItems(List<IEndlessAdaptable> items) {
        return items.size() > 0;
    }
}
