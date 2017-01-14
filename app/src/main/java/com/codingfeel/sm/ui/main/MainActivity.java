package com.codingfeel.sm.ui.main;

import android.animation.Animator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.codingfeel.sm.R;
import com.codingfeel.sm.bean.CommonGuestBean;
import com.codingfeel.sm.event.MessageEvent;
import com.codingfeel.sm.event.SystemEvent;
import com.codingfeel.sm.event.UIEvent;
import com.codingfeel.sm.service.CommonService;
import com.codingfeel.sm.service.UserService;
import com.codingfeel.sm.ui.BaseHideActivity;
import com.codingfeel.sm.ui.info.InfoFragment;
import com.codingfeel.sm.ui.message.MessageFragment;
import com.codingfeel.sm.ui.my.MyFragment;
import com.codingfeel.sm.ui.post.PostFragment;
import com.codingfeel.sm.ui.post.PostHotFragment;
import com.codingfeel.sm.utils.DownloadUtils;
import com.codingfeel.sm.utils.PushUtil;
import com.codingfeel.sm.utils.VerisonUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends BaseHideActivity implements View.OnClickListener {

    @BindView(R.id.llyt_main_container)
    LinearLayout llytMainContainer;
    @BindView(R.id.bnb_main_bottom_menu)
    BottomNavigationBar bnbMainBottomMenu;

    private FragmentManager fragmentManager;

    private PostHotFragment postHotFragment;
    private PostFragment postFragment;
    private MessageFragment messageFragment;
    private MyFragment myFragment;

    private int currentSelectIndex = 0;

    private List<TextView> textViewList = new ArrayList<>();
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();

    private BottomNavigationItem indexItem;
    private BottomNavigationItem postItem;
    private BottomNavigationItem messageItem;
    private BottomNavigationItem myItem;
    private int color;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PushUtil.initPush(this);
        color = getResources().getColor(R.color.themeColor_red);
        UserService.getInstance().autoLogin(this);
//        CommonService.getInstance().homeGuest(this);

        CommonService.getInstance().homeGuest().subscribe(
                onCall -> {

                },
                onError -> {

                },
                () -> {

                });

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initToolbar(View.VISIBLE, View.GONE);
        initView();
        initBottomNavigationBar();
    }


    private void initBottomNavigationBar() {

//        bnbMainBottomMenu.setBarBackgroundColor(R.color.themeColor_red);
//        bnbMainBottomMenu.setActiveColor(R.color.themeColor_red);
//        BadgeItem numberBadgeItem = new BadgeItem()
//                .setBorderWidth(4)
//                .setBackgroundColorResource(R.color.blue)
//                .setText("" + lastSelectedPosition)
//                .setHideOnSelect(autoHide.isChecked());

        bnbMainBottomMenu.setMode(BottomNavigationBar.MODE_SHIFTING);
        bnbMainBottomMenu
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        indexItem = new BottomNavigationItem(R.mipmap.icon_info_white, "热门").setActiveColor(0xffAF141A);
        postItem = new BottomNavigationItem(R.mipmap.icon_post_white, "广场").setActiveColor(0xffCA171E);
        messageItem = new BottomNavigationItem(R.mipmap.icon_msg_gray, "消息").setActiveColor(0xffD91920).setUnReadVisibile(View.VISIBLE);
        myItem = new BottomNavigationItem(R.mipmap.icon_my_gray, "我的").setActiveColor(0xffE81D24);


        bnbMainBottomMenu
                .addItem(indexItem)
                .addItem(postItem)
                .addItem(messageItem)
                .addItem(myItem)
                .initialise();


        bnbMainBottomMenu.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {

                switch (position) {
                    case 0:
                        currentSelectIndex = 0;
                        refreshBottomMenuUI(currentSelectIndex);
                        showFragment(postHotFragment);
                        break;
                    case 1:
                        currentSelectIndex = 1;
                        refreshBottomMenuUI(currentSelectIndex);
                        showFragment(postFragment);
                        break;
                    case 2:
                        bnbMainBottomMenu.hideUnread();
                        currentSelectIndex = 2;
                        refreshBottomMenuUI(currentSelectIndex);
                        showFragment(messageFragment);
                        break;
                    case 3:
                        currentSelectIndex = 3;
                        refreshBottomMenuUI(currentSelectIndex);
                        showFragment(myFragment);
                        break;
                }


            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });
    }


    @Subscribe
    public void onEvent(final SystemEvent.VerisonUpdateEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(event.getCommonGuestBean().getAppVersion())) {
                    int versionCode = VerisonUtil.getVersion(MainActivity.this);
                    int serverVersionCode = Integer.parseInt(event.getCommonGuestBean().getAppVersion().replace(".", ""));

                    if (versionCode < serverVersionCode) {


                        if (Integer.parseInt(event.getCommonGuestBean().getAppIsForceUpdate()) == 1) {
                            materialDialog = new MaterialDialog.Builder(MainActivity.this).title("新版本").positiveColor(color).negativeColor(color).content(event.getCommonGuestBean().getAppUpdateNotes()).cancelable(false).positiveText("更新").onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    if (which == DialogAction.POSITIVE) {
                                        doUpdateVerison("", event.getCommonGuestBean().getAppDownloadUrl());
                                        materialDialog.dismiss();
                                    }

                                }
                            }).build();
                        } else {
                            materialDialog = new MaterialDialog.Builder(MainActivity.this).title("新版本").cancelable(true).positiveColor(color).negativeColor(color).content(event.getCommonGuestBean().getAppUpdateNotes()).positiveText("更新").negativeText("取消").onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    if (which == DialogAction.POSITIVE) {
                                        doUpdateVerison("", event.getCommonGuestBean().getAppDownloadUrl());
                                        materialDialog.dismiss();
                                    } else if (which == DialogAction.NEGATIVE) {
                                        materialDialog.dismiss();
                                    }

                                }
                            }).build();
                        }

                        materialDialog.show();

                    }
                }
            }
        });
    }

    private void doUpdateVerison(String content, final String url) {
        new MaterialDialog.Builder(MainActivity.this)
                .title("新版本")
                .content("正在下载")
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 0, true)
                .cancelable(false)
                .widgetColor(R.color.themeColor_red)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
                        DownloadUtils.downloadFile(MainActivity.this, url, 0, dialog);
                    }
                }).show();
    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        postHotFragment = new PostHotFragment();
        postFragment = new PostFragment();
        messageFragment = new MessageFragment();
        myFragment = new MyFragment();

        fragmentList.add(postHotFragment);
        fragmentList.add(postFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(myFragment);

        fragmentTransaction.add(R.id.llyt_main_container, postHotFragment);
        fragmentTransaction.add(R.id.llyt_main_container, postFragment);
        fragmentTransaction.add(R.id.llyt_main_container, messageFragment);
        fragmentTransaction.add(R.id.llyt_main_container, myFragment);

        fragmentTransaction.commit();

        fragmentTransaction.show(postHotFragment);

    }

    @Subscribe
    public void onEvent(final MessageEvent.NewMessageEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (messageItem.getUnReadVisibile() == View.VISIBLE) {
                    messageItem.setUnReadVisibile(View.GONE);
                } else {
                    messageItem.setUnReadVisibile(View.VISIBLE);
                }
                bnbMainBottomMenu.showUnread();
//                bnbMainBottomMenu.initialise();
            }
        });
    }

    @Subscribe
    public void onEvent(final UIEvent.MainBottomEvent event) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.isShow()) {
                    showViews();
                } else {
                    hideViews();
                }
            }
        });
    }

    private void hideViews() {
//        AnimatorSet animatorSet = new AnimatorSet();
//
//        animatorSet.playTogether(ObjectAnimator.ofFloat(bnbMainBottomMenu, "translationY", +bnbMainBottomMenu.getHeight()),ObjectAnimator.ofFloat(bnbMainBottomMenu, "y", +bnbMainBottomMenu.getHeight())
//                );
//
//        animatorSet.setDuration(1000);
//
////        animatorSet.addListener(listener);
//
//
//        animatorSet.start();

        bnbMainBottomMenu.animate().translationY(+bnbMainBottomMenu.getHeight()).setInterpolator(new AccelerateInterpolator(2)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                bnbMainBottomMenu.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void showViews() {
        bnbMainBottomMenu.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
//                bnbMainBottomMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

    private void refreshBottomMenuUI(int index) {
        for (int i = 0; i < textViewList.size(); i++) {
            if (i == index) {
                textViewList.get(i).setVisibility(View.VISIBLE);
            } else {
                textViewList.get(i).setVisibility(View.GONE);
            }
        }
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i == index) {
                switch (index) {
                    case 0:
                        imageViewList.get(0).setBackgroundResource(R.mipmap.icon_info_white);
                        imageViewList.get(1).setBackgroundResource(R.mipmap.icon_post_gray);
                        imageViewList.get(2).setBackgroundResource(R.mipmap.icon_msg_gray);
                        imageViewList.get(3).setBackgroundResource(R.mipmap.icon_my_gray);
                        break;
                    case 1:
                        imageViewList.get(0).setBackgroundResource(R.mipmap.icon_info_gray);
                        imageViewList.get(1).setBackgroundResource(R.mipmap.icon_post_white);
                        imageViewList.get(2).setBackgroundResource(R.mipmap.icon_msg_gray);
                        imageViewList.get(3).setBackgroundResource(R.mipmap.icon_my_gray);
                        break;
                    case 2:
                        imageViewList.get(0).setBackgroundResource(R.mipmap.icon_info_gray);
                        imageViewList.get(1).setBackgroundResource(R.mipmap.icon_post_gray);
                        imageViewList.get(2).setBackgroundResource(R.mipmap.icon_msg_white);
                        imageViewList.get(3).setBackgroundResource(R.mipmap.icon_my_gray);
                        break;
                    case 3:
                        imageViewList.get(0).setBackgroundResource(R.mipmap.icon_info_gray);
                        imageViewList.get(1).setBackgroundResource(R.mipmap.icon_post_gray);
                        imageViewList.get(2).setBackgroundResource(R.mipmap.icon_msg_gray);
                        imageViewList.get(3).setBackgroundResource(R.mipmap.icon_my_white);
                        break;
                }
            }
        }
    }

    private void showFragment(Fragment f) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {
            if (i != currentSelectIndex) {
                fragmentTransaction.hide(fragmentList.get(i));
            }
        }
        fragmentTransaction.show(f);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        if (materialDialog != null) {
            materialDialog.dismiss();
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {

//        switch (view.getId()) {
//            case R.id.llyt_main_bottom_menu_home:
//                currentSelectIndex = 0;
//                refreshBottomMenuUI(currentSelectIndex);
//                showFragment(infoFragment);
//                break;
//            case R.id.llyt_main_bottom_menu_post:
//                currentSelectIndex = 1;
//                refreshBottomMenuUI(currentSelectIndex);
//                showFragment(postFragment);
//                break;
//            case R.id.llyt_main_bottom_menu_msg:
//                currentSelectIndex = 2;
//                refreshBottomMenuUI(currentSelectIndex);
//                showFragment(messageFragment);
//                break;
//            case R.id.llyt_main_bottom_menu_my:
//                currentSelectIndex = 3;
//                refreshBottomMenuUI(currentSelectIndex);
//                showFragment(myFragment);
//                break;
//        }
    }
}
