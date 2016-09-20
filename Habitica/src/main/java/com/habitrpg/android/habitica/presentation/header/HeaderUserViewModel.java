package com.habitrpg.android.habitica.presentation.header;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.models.Stats;
import com.habitrpg.android.habitica.models.User;

public class HeaderUserViewModel {

    User user;

    private final Context context;
    public String gold;
    public String silver;
    public String gems;
    public Drawable classDrawable;
    public int classDrawableId;
    public String levelString;

    public float hpWeight;
    public String hpValueString;

    public float xpWeight;
    public String xpValueString;

    public float mpWeight;
    public String mpValueString;

    public HeaderUserViewModel(Context context) {
        this.context = context;
    }

    public HeaderUserViewModel setUser(User user) {
        this.user = user;
        Stats stats = user.getStats();
        int gp = (stats.getGp().intValue());
        int sp = (int) ((stats.getGp() - gp) * 100);
        this.gold = String.valueOf(gp);
        this.silver = String.valueOf(sp);
        Double gemCount = user.getBalance() * 4;
        this.gems = String.valueOf(gemCount.intValue());

        this.hpValueString = formatValueString(stats.getHp(), stats.getMaxHealth());
        this.hpWeight = getWeight(stats.getHp(), stats.getMaxHealth());
        this.xpValueString = formatValueString(stats.getExp(), stats.getToNextLevel());
        this.xpWeight = getWeight(stats.getExp(), stats.getToNextLevel());
        this.mpValueString = formatValueString(stats.getMp(), stats.getMaxMP());
        this.mpWeight = getWeight(stats.getMp(), stats.getMaxMP());

        if (user.getPreferences() != null && user.getFlags() != null && (user.getPreferences().getDisableClasses() || !user.getFlags().getClassSelected())) {
            levelString = context.getString(R.string.user_level, user.getStats().getLvl());
        } else {
            levelString= context.getString(R.string.user_level_with_class, user.getStats().getLvl(), stats.get_class());
            int drawableId = 0;
            switch (stats.get_class()) {
                case warrior:
                    drawableId = R.drawable.ic_header_warrior;
                    break;
                case rogue:
                    drawableId = R.drawable.ic_header_rogue;
                    break;
                case wizard:
                    drawableId = R.drawable.ic_header_mage;
                    break;
                case healer:
                    drawableId =  R.drawable.ic_header_healer;
                    break;
                case base:
                default:
                    drawableId = R.drawable.ic_header_warrior;
            }
            if (drawableId != classDrawableId) {
                classDrawableId = drawableId;
                classDrawable = ResourcesCompat.getDrawable(context.getResources(), drawableId, null);
            }
        }
        return this;
    }

    private float getWeight(Double currentValue, Integer maxValue) {
        return (float) Math.min(1, currentValue / maxValue);
    }

    private String formatValueString(Double currentValue, Integer maxValue) {
        return currentValue.intValue() + "/" + maxValue;
    }
}