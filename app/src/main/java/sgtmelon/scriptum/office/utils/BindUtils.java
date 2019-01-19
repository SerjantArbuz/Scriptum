package sgtmelon.scriptum.office.utils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import sgtmelon.scriptum.office.annot.def.ColorDef;
import sgtmelon.scriptum.office.annot.def.ThemeDef;

/**
 * Класс содержащий адаптеры для dataBinding
 */
public final class BindUtils {

    // TODO: 10.12.2018 разобраться с requireAll =
    // TODO: 13.01.2019 комментарии

    /**
     * Установка цветового фильтра на основании результата логического выражения
     *
     * @param boolExpression - Логическое выражение
     * @param trueColor      - Цвет при истине
     * @param falseColor     - Цвет если ложь
     */
    @BindingAdapter(value = {"boolExpression", "trueColor", "falseColor"})
    public static void setTint(@NonNull ImageButton imageButton, boolean boolExpression,
                               @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = imageButton.getContext();

        imageButton.setColorFilter(boolExpression
                        ? ColorUtils.get(context, trueColor)
                        : ColorUtils.get(context, falseColor));
    }

    @BindingAdapter(value = {"boolExpression", "trueColor", "falseColor"})
    public static void setTextColor(@NonNull TextView textView, boolean boolExpression,
                                    @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = textView.getContext();

        textView.setTextColor(boolExpression
                ? ColorUtils.get(context, trueColor)
                : ColorUtils.get(context, falseColor));
    }

    @BindingAdapter("noteColor")
    public static void setCardBackgroundColor(@NonNull CardView cardView, @ColorDef int color) {
        final Context context = cardView.getContext();

        cardView.setCardBackgroundColor(ColorUtils.get(context, color, false));
    }

    @BindingAdapter(value = {"noteColor", "viewOnDark"})
    public static void setTint(@NonNull ImageView imageView, @ColorDef int color,
                               boolean viewOnDark) {
        final Context context = imageView.getContext();

        imageView.setColorFilter(ColorUtils.get(context, color, viewOnDark));
    }

    @BindingAdapter(value = {"imageId", "imageColor"})
    public static void setImage(@NonNull ImageView imageView, @DrawableRes int drawableId,
                                @AttrRes int color) {
        final Context context = imageView.getContext();
        final Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable != null) {
            imageView.setImageDrawable(drawable);
            imageView.setColorFilter(ColorUtils.get(context, color));
        }
    }

    @BindingAdapter("time")
    public static void setNoteTime(@NonNull TextView textView, @NonNull String time) {
        textView.setText(TimeUtils.format(textView.getContext(), time));
    }

    /**
     * Блокирование кнопки и установка на неё цветового фильтра в зависимости
     * от логического выражения и дополнительного выражения
     *
     * @param boolExpression  - Логическое выржение, от которого зависит иконка и блокировка
     * @param extraExpression - Дополнительный параметр для контроля, например список содержит
     *                        текстовое сообщение
     */
    @BindingAdapter(value = {"boolExpression", "extraExpression", "trueColor", "falseColor"})
    public static void setTint(@NonNull ImageButton imageButton, boolean boolExpression,
                               boolean extraExpression,
                               @AttrRes int trueColor, @AttrRes int falseColor) {
        final Context context = imageButton.getContext();

        imageButton.setColorFilter(boolExpression
                        ? extraExpression
                        ? ColorUtils.get(context, trueColor)
                        : ColorUtils.get(context, falseColor)
                        : ColorUtils.get(context, falseColor));

        imageButton.setEnabled(boolExpression && extraExpression);
    }

    @BindingAdapter("enabled")
    public static void setEnabled(@NonNull ImageButton imageButton, boolean enabled) {
        imageButton.setEnabled(enabled);
    }

    @BindingAdapter("visibleOn")
    public static void setVisibility(@NonNull View view, @ThemeDef int visibleTheme) {
        final Context context = view.getContext();
        final int currentTheme = PrefUtils.getInstance(context).getTheme();

        view.setVisibility(currentTheme == visibleTheme
                ? View.VISIBLE
                : View.GONE);
    }

    /**
     * @param toggle - Просто изменить состояние для CheckBox или указать конкретное
     * @param state  - Конкретное состояние для checkBox
     */
    @BindingAdapter(value = {"checkToggle", "checkState"})
    public static void setCheckBoxCheck(@NonNull CheckBox checkBox, boolean toggle,
                                        boolean state) {
        if (toggle) {
            checkBox.toggle();
        } else {
            checkBox.setChecked(state);
        }
    }

}