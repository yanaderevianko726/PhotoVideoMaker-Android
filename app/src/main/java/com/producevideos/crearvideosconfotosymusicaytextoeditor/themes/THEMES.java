package com.producevideos.crearvideosconfotosymusicaytextoeditor.themes;

import com.producevideos.crearvideosconfotosymusicaytextoeditor.R;
import com.producevideos.crearvideosconfotosymusicaytextoeditor.mask.FinalMaskBitmap3D.EFFECT;

import java.util.ArrayList;

public enum THEMES {
    Shine("Shine") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Whole3D_BT);
            mEffects.add(EFFECT.Whole3D_TB);
            mEffects.add(EFFECT.Whole3D_LR);
            mEffects.add(EFFECT.Whole3D_RL);
            mEffects.add(EFFECT.SepartConbine_BT);
            mEffects.add(EFFECT.SepartConbine_TB);
            mEffects.add(EFFECT.SepartConbine_LR);
            mEffects.add(EFFECT.SepartConbine_RL);
            mEffects.add(EFFECT.RollInTurn_BT);
            mEffects.add(EFFECT.RollInTurn_TB);
            mEffects.add(EFFECT.RollInTurn_LR);
            mEffects.add(EFFECT.RollInTurn_RL);
            mEffects.add(EFFECT.Jalousie_BT);
            mEffects.add(EFFECT.Jalousie_LR);
            mEffects.add(EFFECT.Roll2D_BT);
            mEffects.add(EFFECT.Roll2D_TB);
            mEffects.add(EFFECT.Roll2D_LR);
            mEffects.add(EFFECT.Roll2D_RL);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.shine;
        }

        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    Jalousie_BT("Jalousie_BT") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Jalousie_BT);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.jalousie_bt;
        }

        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    Jalousie_LR("Jalousie_LR") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Jalousie_LR);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.jalousie_lr;
        }

        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    Whole3D_BT("Whole3D_BT") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Whole3D_BT);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whole_3d_bt;
        }

        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    Whole3D_TB("Whole3D_TB") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Whole3D_TB);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whole_3d_tb;
        }

        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    Whole3D_LR("Whole3D_LR") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Whole3D_LR);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whole_3d_lr;
        }

        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    Whole3D_RL("Whole3D_Rl") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Whole3D_RL);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.whole_3d_rl;
        }

        public int getThemeMusic() {
            return R.raw._3;
        }
    },
    SepartConbine_BT("SepartConbine_BT") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.SepartConbine_BT);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.separtcon_down;
        }

        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    SepartConbine_TB("SepartConbine_TB") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.SepartConbine_TB);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.separtcon_up;
        }

        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    SepartConbine_LR("SepartConbine_LR") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.SepartConbine_LR);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.separtcon;
        }

        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    SepartConbine_RL("SepartConbine_Rl") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.SepartConbine_RL);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.separtcon_rote;
        }

        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    RollInTurn_RL("RollInTurn_RL") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.RollInTurn_BT);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.rolln_turn_right;
        }

        public int getThemeMusic() {
            return R.raw._3;
        }
    },
    RollInTurn_LR("RollInTurn_LR") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.RollInTurn_TB);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.rolln_turn_left;
        }

        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    RollInTurn_TB("RollInTurn_TB") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.RollInTurn_LR);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.rolln_turn_up;
        }

        public int getThemeMusic() {
            return R.raw._5;
        }
    },
    RollInTurn_BT("RollInTurn_BT") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.RollInTurn_RL);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.rolln_turn_down;
        }

        public int getThemeMusic() {
            return R.raw._1;
        }
    },
    Roll2D_BT("Roll2D_BT") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Roll2D_BT);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.roll2d_bt;
        }

        public int getThemeMusic() {
            return R.raw._2;
        }
    },
    Roll2D_TB("Roll2D_TB") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Roll2D_TB);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.roll2d_tb;
        }

        public int getThemeMusic() {
            return R.raw._3;
        }
    },
    Roll2D_LR("Roll2D_LR") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Roll2D_LR);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.roll2d_lr;
        }

        public int getThemeMusic() {
            return R.raw._4;
        }
    },
    Roll2D_Rl("Roll2D_Rl") {
        public ArrayList<EFFECT> getTheme() {
            ArrayList<EFFECT> mEffects = new ArrayList();
            mEffects.add(EFFECT.Roll2D_RL);
            return mEffects;
        }

        public ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList) {
            return null;
        }

        public int getThemeDrawable() {
            return R.drawable.roll2d_rl;
        }

        public int getThemeMusic() {
            return R.raw._5;
        }
    };
    
    String name;

    public abstract ArrayList<EFFECT> getTheme();

    public abstract ArrayList<EFFECT> getTheme(ArrayList<EFFECT> arrayList);

    public abstract int getThemeDrawable();

    public abstract int getThemeMusic();

    private THEMES(String string) {
        this.name = "";
        this.name = string;
    }
}
