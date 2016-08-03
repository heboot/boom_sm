package com.codingfeel.sm.anim;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.codingfeel.sm.utils.LogUtils;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Heboot on 16/7/31.
 */
public class MyItemAnimator extends SimpleItemAnimator {

    List<RecyclerView.ViewHolder> mAnimationRemoveViewHolders = new ArrayList<RecyclerView.ViewHolder>();


    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preLayoutInfo, @NonNull ItemHolderInfo postLayoutInfo) {
        LogUtils.e("========MyItemAnimator==", "=========animateChange==");
        return false;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        LogUtils.e("========MyItemAnimator==", "=========animateRemove==");
        return mAnimationRemoveViewHolders.add(holder);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        LogUtils.e("========MyItemAnimator==", "=========animateAdd==");
        return false;
    }

    @Override
    public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
        LogUtils.e("========MyItemAnimator==", "=========animateMove==");
        return false;
    }

    @Override
    public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
        LogUtils.e("========MyItemAnimator==", "=========animateChange==");
        return false;
    }


    @Override
    public void runPendingAnimations() {
        LogUtils.e("========MyItemAnimator==", "=========runPendingAnimations==");
        View target;
        if (!mAnimationRemoveViewHolders.isEmpty()) {
            for (final RecyclerView.ViewHolder viewHolder : mAnimationRemoveViewHolders) {
                target = viewHolder.itemView;

                YoYo.with(Techniques.SlideOutLeft).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimationRemoveViewHolders.remove(viewHolder);
                        if (isRunning()) {
                            dispatchAnimationsFinished();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).duration(1000).playOn(target);

            }

        }
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {

    }

    @Override
    public void endAnimations() {

    }

    @Override
    public boolean isRunning() {
        return !(mAnimationRemoveViewHolders.isEmpty());
    }
}
