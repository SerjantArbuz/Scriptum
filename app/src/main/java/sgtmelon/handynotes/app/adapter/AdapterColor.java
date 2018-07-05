package sgtmelon.handynotes.app.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Arrays;

import sgtmelon.handynotes.R;
import sgtmelon.handynotes.office.Help;
import sgtmelon.handynotes.databinding.ItemColorBinding;

public class AdapterColor extends RecyclerView.Adapter<AdapterColor.ColorHolder> {

    //region Variables
    private final Context context;

    private int check;
    private final boolean[] visible;
    //endregion

    public AdapterColor(Context context, int check) {
        this.context = context;

        this.check = check;

        visible = new boolean[getItemCount()];
        Arrays.fill(visible, false);
        visible[check] = true;
    }

    public int getCheck() {
        return check;
    }

    @NonNull
    @Override
    public ColorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemColorBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_color, parent, false);
        return new ColorHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorHolder holder, int position) {
        holder.bind(position);

        if (visible[position]) {                                   //Если отметка видна
            if (this.check == position) {                            //Если текущая позиция совпадает с выбранным цветом
                holder.clCheck.setVisibility(View.VISIBLE);
            } else {
                visible[position] = false;                         //Делаем отметку невидимой с анимацией
                holder.clCheck.startAnimation(holder.alphaOut);
            }
        } else holder.clCheck.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return Help.Icon.getColorLength();
    }

    class ColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Animation.AnimationListener {

        private final ImageView clClick, clCheck;
        private final Animation alphaIn, alphaOut;

        private final ItemColorBinding binding;

        ColorHolder(ItemColorBinding binding) {
            super(binding.getRoot());

            this.binding = binding;

            clClick = itemView.findViewById(R.id.itemColor_iv_click);
            clCheck = itemView.findViewById(R.id.itemColor_iv_check);

            clClick.setOnClickListener(this);

            alphaIn = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
            alphaOut = AnimationUtils.loadAnimation(context, R.anim.alpha_out);

            alphaIn.setAnimationListener(this);
            alphaOut.setAnimationListener(this);
        }

        void bind(int position) {
            binding.setPosition(position);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            int oldCheck = check;                //Сохраняем старую позицию
            int newCheck = getAdapterPosition();    //Получаем новую

            if (oldCheck != newCheck) {             //Если выбранный цвет не совпадает с тем, на который нажали
                check = newCheck;                //Присваиваем новую позицию
                visible[check] = true;

                notifyItemChanged(oldCheck);        //Скрываем старую отметку
                clCheck.startAnimation(alphaIn);      //Показываем новую
            }
        }

        @Override
        public void onAnimationStart(Animation animation) {
            clClick.setEnabled(false);

            if (animation == alphaIn) {
                clCheck.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            clClick.setEnabled(true);

            if (animation == alphaOut) {
                clCheck.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}