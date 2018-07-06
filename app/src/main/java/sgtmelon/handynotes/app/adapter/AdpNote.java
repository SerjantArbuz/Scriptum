package sgtmelon.handynotes.app.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.app.model.item.ItemNote;
import sgtmelon.handynotes.app.model.item.ItemRoll;
import sgtmelon.handynotes.app.model.repo.RepoNote;
import sgtmelon.handynotes.databinding.ItemNoteRollBinding;
import sgtmelon.handynotes.databinding.ItemNoteTextBinding;
import sgtmelon.handynotes.office.annot.def.db.DefType;
import sgtmelon.handynotes.office.intf.IntfItem;

public class AdpNote extends RecyclerView.Adapter<AdpNote.NoteHolder> {

    private final List<RepoNote> listRepoNote;

    public AdpNote() {
        listRepoNote = new ArrayList<>();
    }

    private IntfItem.Click click;
    private IntfItem.LongClick longClick;

    public void setCallback(IntfItem.Click click, IntfItem.LongClick longClick) {
        this.click = click;
        this.longClick = longClick;
    }

    public void update(List<RepoNote> listRepoNote) {
        this.listRepoNote.clear();
        this.listRepoNote.addAll(listRepoNote);
    }

    @Override
    public int getItemViewType(int position) {
        return listRepoNote.get(position).getItemNote().getType();
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DefType.text) {
            ItemNoteTextBinding bindingText = DataBindingUtil.inflate(inflater, R.layout.item_note_text, parent, false);
            return new NoteHolder(bindingText);
        } else {
            ItemNoteRollBinding bindingRoll = DataBindingUtil.inflate(inflater, R.layout.item_note_roll, parent, false);
            return new NoteHolder(bindingRoll);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        RepoNote repoNote = listRepoNote.get(position);
        ItemNote itemNote = repoNote.getItemNote();

        if (itemNote.getType() == DefType.text) {
            holder.bind(itemNote, null);
        } else {
            holder.bind(itemNote, repoNote.getListRoll());
        }
    }

    @Override
    public int getItemCount() {
        return listRepoNote.size();
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final View ntClick;

        private final ItemNoteTextBinding bindingText;
        private final ItemNoteRollBinding bindingRoll;

        NoteHolder(ItemNoteTextBinding bindingText) {
            super(bindingText.getRoot());

            this.bindingText = bindingText;
            bindingRoll = null;

            ntClick = itemView.findViewById(R.id.itemNote_ll_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        NoteHolder(ItemNoteRollBinding bindingRoll) {
            super(bindingRoll.getRoot());

            this.bindingRoll = bindingRoll;
            bindingText = null;

            ntClick = itemView.findViewById(R.id.itemNote_ll_click);

            ntClick.setOnClickListener(this);
            ntClick.setOnLongClickListener(this);
        }

        void bind(ItemNote itemNote, List<ItemRoll> listRoll) {
            if (itemNote.getType() == DefType.text) {
                bindingText.setItemNote(itemNote);

                bindingText.executePendingBindings();
            } else {
                bindingRoll.setItemNote(itemNote);
                bindingRoll.setListRoll(listRoll);

                bindingRoll.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            click.onItemClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            longClick.onItemLongClick(view, getAdapterPosition());
            return true;
        }
    }
}