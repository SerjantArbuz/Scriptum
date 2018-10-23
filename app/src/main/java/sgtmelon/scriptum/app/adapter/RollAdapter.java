package sgtmelon.scriptum.app.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import sgtmelon.scriptum.R;
import sgtmelon.scriptum.app.model.item.RollItem;
import sgtmelon.scriptum.app.view.fragment.RollFragment;
import sgtmelon.scriptum.databinding.ItemRollReadBinding;
import sgtmelon.scriptum.databinding.ItemRollWriteBinding;
import sgtmelon.scriptum.office.annot.def.RollTypeDef;
import sgtmelon.scriptum.office.st.NoteSt;

/**
 * Адаптер для {@link RollFragment}
 */
public final class RollAdapter extends ParentAdapter<RollItem, RollAdapter.RollHolder> {

    private NoteSt noteSt;

    public RollAdapter(Context context) {
        super(context);
    }

    public void setNoteSt(NoteSt noteSt) {
        this.noteSt = noteSt;
    }

    @NonNull
    @Override
    public RollHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            default:
            case RollTypeDef.read:
                ItemRollReadBinding bindingRead = DataBindingUtil.inflate(
                        inflater, R.layout.item_roll_read, parent, false
                );
                return new RollHolder(bindingRead);
            case RollTypeDef.write:
                ItemRollWriteBinding bindingWrite = DataBindingUtil.inflate(
                        inflater, R.layout.item_roll_write, parent, false
                );
                return new RollHolder(bindingWrite);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RollHolder holder, int position) {
        RollItem item = list.get(position);
        holder.bind(item);
    }

    @BindingAdapter("colorFilter")
    public static void setColorFilter(ImageButton imageButton, int color){
        imageButton.setColorFilter(color);
    }

    @Override
    public int getItemViewType(int position) {
        if (!noteSt.isEdit()) return RollTypeDef.read;
        else return RollTypeDef.write;
    }

    final class RollHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnTouchListener, TextWatcher {

        private final ItemRollWriteBinding bindingWrite;
        private final ItemRollReadBinding bindingRead;

        private EditText rlEnter;
        private View rlDrag; //Кнопка для перетаскивания (< >)

        private CheckBox rlCheck;   //Отметка о выполении
        private View rlClick;  //Кнопка, которая идёт поверх rlCheck, для полноценного эффекта нажатия

        RollHolder(ItemRollWriteBinding bindingWrite) {
            super(bindingWrite.getRoot());

            this.bindingWrite = bindingWrite;
            bindingRead = null;

            rlEnter = itemView.findViewById(R.id.roll_enter);
            rlDrag = itemView.findViewById(R.id.drag_button);

            rlEnter.setOnTouchListener(this);
            rlEnter.addTextChangedListener(this);

            rlDrag.setOnTouchListener(this);
        }

        RollHolder(ItemRollReadBinding bindingRead) {
            super(bindingRead.getRoot());

            this.bindingRead = bindingRead;
            bindingWrite = null;

            rlCheck = itemView.findViewById(R.id.roll_check);
            rlClick = itemView.findViewById(R.id.click_view);

            rlClick.setOnClickListener(this);
        }

        void bind(RollItem rollItem) {
            if (noteSt.isEdit()) {
                bindingWrite.setRollItem(rollItem);
                bindingWrite.executePendingBindings();
            } else {
                bindingRead.setRollItem(rollItem);
                bindingRead.setKeyBin(noteSt.isBin());
                bindingRead.executePendingBindings();
            }
        }

        @Override
        public void onClick(View view) {
            if (!noteSt.isEdit()) {
                int p = getAdapterPosition();
                rlCheck.setChecked(!list.get(p).isCheck());
                clickListener.onItemClick(view, p);
            }
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                dragListener.setItemDrag(view.getId() == R.id.drag_button);
            }
            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            watcher.onChanged(getAdapterPosition(), rlEnter.getText().toString());
        }

    }

}
