package com.dk.dkhome.presenter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dk.dkhome.R;
import com.dk.dkhome.entity.EquipData;
import com.dk.dkhome.entity.Food;
import com.dk.dkhome.entity.FoodType;
import com.dk.dkhome.entity.User;
import com.dk.dkhome.utils.AnimManager;
import com.dk.dkhome.utils.FoodManager;
import com.dk.dkhome.view.DkhomeApplication;
import com.dk.dkhome.view.activity.FoodActivity;
import com.dk.dkhome.view.activity.MainActivity;
import com.dk.dkhome.view.activity.RegisterActivity;
import com.dk.dkhome.view.adapter.FoodAdapter;
import com.dk.dkhome.view.adapter.SportPageAdapter;

import java.util.ArrayList;

import intersky.appbase.Presenter;
import intersky.apputils.AppUtils;
import intersky.mywidget.NoScrollViewPager;


public class FoodPresenter implements Presenter {

    public FoodActivity mFoodActivity;
    public SportPageAdapter sportPageAdapter;
    public NoScrollViewPager noScrollViewPager;
    public FoodAdapter foodAdapter;
    public View food1;
    public View food2;
    public LinearLayout typeList;
    public RecyclerView foodList;
    public TextView daycarl;
    public LinearLayout foodSelect;
    public TextView hit;
    public RelativeLayout btnCar;
    public RelativeLayout btnFood;
    public ImageView imgFood;
    public ImageView imgCar;
    public TextView txtFood;
    public TextView txtCar;

    public FoodPresenter(FoodActivity mFoodActivity) {
        this.mFoodActivity = mFoodActivity;
    }

    @Override
    public void initView() {
        // TODO Autonerated method stub
        mFoodActivity.setContentView(R.layout.activity_food);
        LayoutInflater inflater = LayoutInflater.from(mFoodActivity);
        ImageView back = mFoodActivity.findViewById(R.id.back);
        back.setOnClickListener(mFoodActivity.mBackListener);
        foodAdapter = new FoodAdapter(mFoodActivity);
        foodAdapter.mFoods.clear();
        foodAdapter.setOnItemClickListener(foodClickListener);
        food1 = inflater.inflate(R.layout.fragment_foodselect, null);
        food2 = inflater.inflate(R.layout.fragment_result, null);
        noScrollViewPager = mFoodActivity.findViewById(R.id.pager);
        typeList = food1.findViewById(R.id.foodtypelist);
        foodList = food1.findViewById(R.id.foodlist);
        foodList.setLayoutManager(new LinearLayoutManager(mFoodActivity));
        foodList.setAdapter(foodAdapter);
        foodSelect = food2.findViewById(R.id.foodselect);
        daycarl = food2.findViewById(R.id.sptortcarlvalue);
        hit = mFoodActivity.findViewById(R.id.hit);
        TextView save = mFoodActivity.findViewById(R.id.btnsave);
        btnCar = mFoodActivity.findViewById(R.id.btncar);
        btnFood = mFoodActivity.findViewById(R.id.btnfood);
        imgCar = mFoodActivity.findViewById(R.id.cartimg);
        imgFood = mFoodActivity.findViewById(R.id.foodtimg);
        txtCar = mFoodActivity.findViewById(R.id.cartext);
        txtFood = mFoodActivity.findViewById(R.id.foodtext);

        save.setOnClickListener(finishListener);
        sportPageAdapter = new SportPageAdapter();
        sportPageAdapter.mViews.add(food1);
        sportPageAdapter.mViews.add(food2);
        noScrollViewPager.setNoScroll(true);
        noScrollViewPager.setAdapter(sportPageAdapter);

        btnCar.setOnClickListener(carListener);
        btnFood.setOnClickListener(foodListener);

        txtCar.setTextColor(Color.parseColor("#333333"));
        txtFood.setTextColor(Color.parseColor(                                                                                                                                                                                 "#ff5e3a"));
        imgCar.setImageResource(R.drawable.car);
        imgFood.setImageResource(R.drawable.foods);
        noScrollViewPager.setCurrentItem(0);
    }


    @Override
    public void Start() {
        // TODO Auto-generated method stub

    }


    @Override
    public void Resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Destroy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void Create() {
        // TODO Auto-generated method stub
        initView();
        initFoodtype();
        initSelectFood();
    }

    private void initFoodtype() {
        LayoutInflater mInflater = (LayoutInflater) mFoodActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int i = 0 ; i < FoodManager.foodManager.foodtype.size() ; i++)
        {
            FoodType foodType = FoodManager.foodManager.foodtype.get(i);
            foodType.view = mInflater.inflate(R.layout.item_foodtype,null);
            TextView typename = foodType.view.findViewById(R.id.foodtype_name);
            ImageView imageView = foodType.view.findViewById(R.id.foodtype);
            typename.setText(foodType.name);
            imageView.setImageResource(FoodManager.foodManager.getFoodImgSource(foodType.uid));
            RelativeLayout main = foodType.view.findViewById(R.id.main);
            if(foodType.isselect)
            {
                main.setBackgroundColor(Color.parseColor("#fce88b"));
            }
            else
            {
                main.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            main.setOnClickListener(setFoodListListener);
            main.setTag(foodType);
            typeList.addView(foodType.view);
        }
        foodAdapter.mFoods.addAll(FoodManager.foodManager.typeFood.get(FoodManager.foodManager.selectFoodtype.uid));
        foodAdapter.notifyDataSetChanged();
    }

    private void initSelectFood()
    {
        for(int i = 0 ; i < FoodManager.foodManager.selectFoods.size(); i++) {
            Food food = FoodManager.foodManager.selectFoods.get(i);
            addSelectView(food);
        }
        daycarl.setText(String.format("%d kCal",FoodManager.foodManager.totalCarl));
    }

    private void addSelectView(Food food) {
        LayoutInflater mInflater = (LayoutInflater) mFoodActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        food.view = mInflater.inflate(R.layout.item_food_select,null);
        TextView typename = food.view.findViewById(R.id.foodselect_name);
        ImageView imageView = food.view.findViewById(R.id.foodselect);
        TextView carl = food.view.findViewById(R.id.foodselect_car);
        TextView count = food.view.findViewById(R.id.foodselect_count);
        ImageView add = food.view.findViewById(R.id.add);
        ImageView des = food.view.findViewById(R.id.des);
        add.setTag(food);
        des.setTag(food);
        add.setOnClickListener(addListener);
        des.setOnClickListener(desListener);
        typename.setText(food.name);
        carl.setText(String.format("%d kCal / %d g",food.carl*food.count,food.weight*food.count));
        count.setText(String.valueOf(food.count));
        imageView.setImageResource(FoodManager.foodManager.getFoodImgSource(food.typeid));
        foodSelect.addView(food.view,0);
    }

    private void deleteView(Food food) {
        if(food.view != null)
        {
            foodSelect.removeView(food.view);
        }
    }

    private void updtaView(Food food) {
        if(food.view != null)
        {
            TextView count = food.view.findViewById(R.id.foodselect_count);
            TextView carl = food.view.findViewById(R.id.foodselect_car);
            count.setText(String.valueOf(food.count));
            carl.setText(String.format("%d kCal / %d g",food.carl*food.count,food.weight*food.count));
        }
    }

    private View.OnClickListener setFoodListListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FoodType foodType = (FoodType) v.getTag();
            FoodManager.foodManager.selectFoodtype.isselect = false;
            if(FoodManager.foodManager.selectFoodtype.view != null)
            {
                RelativeLayout main = foodType.view.findViewById(R.id.main);
                if(foodType.isselect)
                {
                    main.setBackgroundColor(Color.parseColor("#fce88b"));
                }
                else
                {
                    main.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
            FoodManager.foodManager.selectFoodtype = foodType;
            if(FoodManager.foodManager.selectFoodtype.view != null)
            {
                RelativeLayout main = foodType.view.findViewById(R.id.main);
                if(foodType.isselect)
                {
                    main.setBackgroundColor(Color.parseColor("#fce88b"));
                }
                else
                {
                    main.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
            foodAdapter.mFoods.clear();
            foodAdapter.mFoods.addAll(FoodManager.foodManager.typeFood.get(FoodManager.foodManager.selectFoodtype.uid));
            foodAdapter.notifyDataSetChanged();
        }

    };

    private FoodAdapter.OnItemClickListener foodClickListener = new FoodAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Food food, int position, View view) {
            if(food.count == 0)
            {
                food.count++;
                FoodManager.foodManager.selectFoods.add(food);
                addSelectView(food);
            }
            else
            {
                food.count++;
            }
            FoodManager.foodManager.totalCarl+= food.carl;
            daycarl.setText(String.format("%d kCal",FoodManager.foodManager.totalCarl));
            AnimManager animManager = new AnimManager.Builder()
                    .with(mFoodActivity)
                    .animModule(AnimManager.AnimModule.BIG_CIRCLE)//图片的动画模式，小的或者大的（仿每日优鲜）
                    .startView(view)//开始位置的控件
                    .endView(hit)//结束位置的控件
                    .listener(new AnimManager.AnimListener() {
                        @Override
                        public void setAnimBegin(AnimManager a) {

                        }

                        @Override
                        public void setAnimEnd(AnimManager a) {
                            //购物车回弹动画（这里是加入购物车动画执行结束时的回调我在这里加入了购物车回弹效果，不需要的话可以去掉）
                            TranslateAnimation anim = new TranslateAnimation(0, 0, 20, 0);
                            anim.setInterpolator(new BounceInterpolator());
                            anim.setDuration(700);
                            imgCar.startAnimation(anim);
                            if(FoodManager.foodManager.selectFoods.size() > 0)
                                hit.setText(String.valueOf(FoodManager.foodManager.selectFoods.size()));
                            else
                                hit.setVisibility(View.INVISIBLE);
                        }
                    })
                    .src(FoodManager.foodManager.getFoodImgSource(food.typeid))
                    .build();
            animManager.startAnim();
        }
    };

    private View.OnClickListener finishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FoodManager.foodManager.saveSelectFood();
            Intent intent = new Intent(FoodManager.ACTION_UPDATA_FOOD);
            mFoodActivity.sendBroadcast(intent);
            mFoodActivity.finish();
        }
    };


    public View.OnClickListener carListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txtCar.setTextColor(Color.parseColor("#ff5e3a"));
            txtFood.setTextColor(Color.parseColor("#333333"));
            imgCar.setImageResource(R.drawable.cars);
            imgFood.setImageResource(R.drawable.food);
            noScrollViewPager.setCurrentItem(1);
        }
    };

    public View.OnClickListener foodListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            txtCar.setTextColor(Color.parseColor("#333333"));
            txtFood.setTextColor(Color.parseColor("#ff5e3a"));
            imgCar.setImageResource(R.drawable.car);
            imgFood.setImageResource(R.drawable.foods);
            noScrollViewPager.setCurrentItem(0);
        }
    };

    public View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Food food = (Food) v.getTag();
            food.count++;
            updtaView(food);
            FoodManager.foodManager.totalCarl+= food.carl;
            daycarl.setText(String.format("%d kCal",FoodManager.foodManager.totalCarl));
            if(FoodManager.foodManager.selectFoods.size() > 0)
                hit.setText(String.valueOf(FoodManager.foodManager.selectFoods.size()));
            else
                hit.setVisibility(View.INVISIBLE);
        }
    };

    public View.OnClickListener desListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Food food = (Food) v.getTag();
            if(food.count == 1)
            {
                food.count--;
                FoodManager.foodManager.selectFoods.remove(food);
                deleteView(food);
            }
            else
            {
                food.count--;
                updtaView(food);
            }
            FoodManager.foodManager.totalCarl-= food.carl;
            daycarl.setText(String.format("%d kCal",FoodManager.foodManager.totalCarl));
            if(FoodManager.foodManager.selectFoods.size() > 0)
            hit.setText(String.valueOf(FoodManager.foodManager.selectFoods.size()));
            else
                hit.setVisibility(View.INVISIBLE);

        }
    };
}
