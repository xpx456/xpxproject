package com.dk.dkhome.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dk.dkhome.R;
import com.dk.dkhome.database.DBHelper;
import com.dk.dkhome.entity.Eat;
import com.dk.dkhome.entity.Food;
import com.dk.dkhome.entity.FoodType;
import com.dk.dkhome.entity.UserDefine;
import com.dk.dkhome.view.DkhomeApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FoodManager {

    public static final String ACTION_UPDATA_FOOD = "ACTION_UPDATA_FOOD";
    public static volatile FoodManager foodManager;
    public HashMap<String, ArrayList<Food>> typeFood = new HashMap<String, ArrayList<Food>>();
    public HashMap<String, Food> allFood = new HashMap<String, Food>();
    public ArrayList<FoodType> foodtype = new ArrayList<FoodType>();
    public HashMap<String, Eat> dayEat = new HashMap<String, Eat>();
    public FoodType selectFoodtype;
    public Eat today;
    public ArrayList<Food> selectFoods = new ArrayList<Food>();
    public int totalCarl = 0;

    public static FoodManager init() {
        if (foodManager == null) {
            synchronized (FoodManager.class) {
                if (foodManager == null) {
                    foodManager = new FoodManager();
                    foodManager.initFood();
                    foodManager.initSelectFood();
                    foodManager.today = DBHelper.getInstance(DkhomeApplication.mApp).scanEat(foodManager.dayEat);
                } else {
                    foodManager.typeFood.clear();
                    foodManager.initFood();
                    foodManager.initSelectFood();
                    foodManager.today = DBHelper.getInstance(DkhomeApplication.mApp).scanEat(foodManager.dayEat);
                }
            }
        }
        return foodManager;
    }

    public double getTotalCarl() {
        double carl = 0;
        for(Map.Entry<String,Eat> temp : dayEat.entrySet()){
            Eat eat = temp.getValue();
            carl += eat.carl;
        }
        return carl;
    }

    public int getFoodImgSource(String type) {
        int itype = Integer.valueOf(type);
        int id = -1;
        switch (itype) {
            case 1:
                id = R.drawable.food1;
                break;
            case 2:
                id = R.drawable.food2;
                break;
            case 3:
                id = R.drawable.food3;
                break;
            case 4:
                id = R.drawable.food4;
                break;
            case 5:
                id = R.drawable.food5;
                break;
            case 6:
                id = R.drawable.food6;
                break;
            case 7:
                id = R.drawable.food7;
                break;
            case 8:
                id = R.drawable.food8;
                break;
            case 9:
                id = R.drawable.food9;
                break;
            case 10:
                id = R.drawable.food10;
                break;
            case 11:
                id = R.drawable.food11;
                break;
            case 12:
                id = R.drawable.food12;
                break;
            case 13:
                id = R.drawable.food13;
                break;


        }
        return id;
    }

    public void saveSelectFood() {
        SharedPreferences user = DkhomeApplication.mApp.getSharedPreferences(DkhomeApplication.mApp.mAccount.uid, 0);
        SharedPreferences.Editor userEditor = user.edit();
        userEditor.putString(UserDefine.USER_FOOD, praseSelectJson());
        userEditor.commit();
        if(today == null)
        {
            today = DBHelper.getInstance(DkhomeApplication.mApp).scanEat(foodManager.dayEat);
        }
        today.json = praseSelectJson();
        today.carl = totalCarl;
        DBHelper.getInstance(DkhomeApplication.mApp).updataEat(today);
    }

    private void initSelectFood() {
        selectFoods.clear();
        totalCarl = 0;
        SharedPreferences user = DkhomeApplication.mApp.getSharedPreferences(DkhomeApplication.mApp.mAccount.uid, 0);
        String data = user.getString(UserDefine.USER_FOOD, "");
        praseSelectFood(data);
    }

    private String praseSelectJson() {

        try {
            JSONArray ja = new JSONArray();
            for (int i = 0; i < selectFoods.size(); i++) {
                Food food = selectFoods.get(i);
                JSONObject jo = new JSONObject();
                jo.put("id", food.uid);
                jo.put("count", food.count);
                ja.put(jo);
            }
            return ja.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    private void praseSelectFood(String json) {
        if (json.length() > 0) ;
        {
            try {
                JSONArray ja = new JSONArray(json);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    String id = jo.getString("id");
                    int count = jo.getInt("count");
                    Food food = allFood.get(id);
                    food.count = count;
                    totalCarl += food.carl * food.count;
                    selectFoods.add(food);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void initFood() {
        typeFood.clear();
        allFood.clear();
        foodtype.clear();

        String[] tname1 = name1.split(",");
        String[] tname2 = name2.split(",");
        String[] tname3 = name3.split(",");
        String[] tname4 = name4.split(",");
        String[] tname5 = name5.split(",");
        String[] tname6 = name6.split(",");
        String[] tname7 = name7.split(",");
        String[] tname8 = name8.split(",");
        String[] tname9 = name9.split(",");
        String[] tname10 = name10.split(",");
        String[] tname11 = name11.split(",");
        String[] tname12 = name12.split(",");
        String[] tname13 = name13.split(",");

        String[] tdata1 = data1.split(",");
        String[] tdata2 = data2.split(",");
        String[] tdata3 = data3.split(",");
        String[] tdata4 = data4.split(",");
        String[] tdata5 = data5.split(",");
        String[] tdata6 = data6.split(",");
        String[] tdata7 = data7.split(",");
        String[] tdata8 = data8.split(",");
        String[] tdata9 = data9.split(",");
        String[] tdata10 = data10.split(",");
        String[] tdata11 = data11.split(",");
        String[] tdata12 = data12.split(",");
        String[] tdata13 = data13.split(",");
        ArrayList<Food> type = new ArrayList<>();
        String typename = "五谷类,豆类";
        FoodType foodType = new FoodType(typename);
        foodType.uid = "1";
        foodType.isselect = true;
        selectFoodtype = foodType;
        for (int i = 0; i < tname1.length; i++) {
            String[] carl = tdata1[i].split("/");
            Food food = new Food(tname1[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "蔬菜类";
        foodType = new FoodType(typename);
        foodType.uid = "2";
        for (int i = 0; i < tname2.length; i++) {
            String[] carl = tdata2[i].split("/");
            Food food = new Food(tname2[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "水果类";
        foodType = new FoodType(typename);
        foodType.uid = "3";
        for (int i = 0; i < tname3.length; i++) {
            String[] carl = tdata3[i].split("/");
            Food food = new Food(tname3[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "肉类";
        foodType = new FoodType(typename);
        foodType.uid = "4";
        for (int i = 0; i < tname4.length; i++) {
            String[] carl = tdata4[i].split("/");
            Food food = new Food(tname4[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "蛋类";
        foodType = new FoodType(typename);
        foodType.uid = "5";
        for (int i = 0; i < tname5.length; i++) {
            String[] carl = tdata5[i].split("/");
            Food food = new Food(tname5[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "水产类";
        foodType = new FoodType(typename);
        foodType.uid = "6";
        for (int i = 0; i < tname6.length; i++) {
            String[] carl = tdata6[i].split("/");
            Food food = new Food(tname6[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "奶类";
        foodType = new FoodType(typename);
        foodType.uid = "7";
        for (int i = 0; i < tname7.length; i++) {
            String[] carl = tdata7[i].split("/");
            Food food = new Food(tname7[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "油脂类";
        foodType = new FoodType(typename);
        foodType.uid = "8";
        for (int i = 0; i < tname8.length; i++) {
            String[] carl = tdata8[i].split("/");
            Food food = new Food(tname8[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "糕点小吃";
        foodType = new FoodType(typename);
        foodType.uid = "9";
        for (int i = 0; i < tname9.length; i++) {
            String[] carl = tdata9[i].split("/");
            Food food = new Food(tname9[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "糖类";
        foodType = new FoodType(typename);
        foodType.uid = "10";
        for (int i = 0; i < tname10.length; i++) {
            String[] carl = tdata10[i].split("/");
            Food food = new Food(tname10[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "饮料类";
        foodType = new FoodType(typename);
        foodType.uid = "11";
        for (int i = 0; i < tname11.length; i++) {
            String[] carl = tdata11[i].split("/");
            Food food = new Food(tname11[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "茵藻类";
        foodType = new FoodType(typename);
        foodType.uid = "12";
        for (int i = 0; i < tname12.length; i++) {
            String[] carl = tdata12[i].split("/");
            Food food = new Food(tname12[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);

        type = new ArrayList<>();
        typename = "其它食品";
        foodType = new FoodType(typename);
        foodType.uid = "13";
        for (int i = 0; i < tname13.length; i++) {
            String[] carl = tdata13[i].split("/");
            Food food = new Food(tname13[i], typename, Integer.valueOf(carl[0]), Integer.valueOf(carl[1]), foodType.uid);
            food.uid = foodType.uid + "-" + String.valueOf(i);
            type.add(food);
            allFood.put(food.uid, food);
        }
        typeFood.put(foodType.uid, type);
        foodtype.add(foodType);
    }




    /*
    String[] name1 = name1.split(",");
        String[] name2 = name2.split(",");
        String[] name3 = name3.split(",");
        String[] name4 = name4.split(",");
        String[] name5 = name5.split(",");
        String[] name6 = name6.split(",");
        String[] name7 = name7.split(",");
        String[] name8 = name8.split(",");
        String[] name9 = name9.split(",");
        String[] name10 = name10.split(",");
        String[] name11 = name11.split(",");
        String[] name12 = name12.split(",");
        String[] name13 = name13.split(",");

        String[] data1 = data1.split(",");
        String[] data2 = data2.split(",");
        String[] data3 = data3.split(",");
        String[] data4 = data4.split(",");
        String[] data5 = data5.split(",");
        String[] data6 = data6.split(",");
        String[] data7 = data7.split(",");
        String[] data8 = data8.split(",");
        String[] data9 = data9.split(",");
        String[] data10 = data10.split(",");
        String[] data11 = data11.split(",");
        String[] data12 = data12.split(",");
        String[] data13 = data13.split(",");

        boolean same = true;
        if(data1.length != name1.length)
            same = false;
        if(data2.length != name2.length)
            same = false;
        if(data3.length != name3.length)
            same = false;
        if(data4.length != name4.length)
            same = false;
        if(data5.length != name5.length)
            same = false;
        if(data6.length != name6.length)
            same = false;
        if(data7.length != name7.length)
            same = false;
        if(data8.length != name8.length)
            same = false;
        if(data9.length != name9.length)
            same = false;
        if(data10.length != name10.length)
            same = false;
        if(data11.length != name11.length)
            same = false;
        if(data12.length != name12.length)
            same = false;
        if(data13.length != name13.length)
            same = false;
    * */

    //五谷类,豆类
    public static final String name1 = "油炸土豆片,黑芝麻,芝麻(白),油面筋,方便面,油饼,油条,莜麦面,燕麦片,小米,薏米,籼米(标一),高粱米,富强粉,通心粉," +
            "大黄米(黍),江米,粳米(标二),挂面(富强粉),机米,玉米糁,米粉(干，细),香大米,籼米(标二),挂面(标准粉),标准粉,血糯米,粳米(标一),黄米,玉米面(白),玉米面(黄)," +
            "素虾(炸),腐竹皮,腐竹,豆浆粉,黄豆粉,豆腐皮,油炸豆瓣,油炸豆花,黑豆,黄豆,蚕豆(干，去皮),卤干,虎皮芸豆,绿豆面,绿豆,杂豆,红芸豆,豌豆(干),红小豆,杂芸豆(带皮)," +
            "蚕豆(干，带皮),白芸豆,油豆腐,白薯干,土豆粉,粉条,地瓜粉,玉米(白),玉米(黄),粉丝,黑米,煎饼,大麦,荞麦粉,烧饼(糖),富强粉切面,标准粉切面,烙饼,馒头(蒸，标准粉)," +
            "麸皮,花卷,馒头(蒸，富强粉),水面筋,烤麸,米饭(蒸，粳米),米饭(蒸，籼米),面条(煮，富强粉),鲜玉米,白薯(白心),白薯(红心),粉皮,小米粥,米粥(粳米),豆沙,红豆馅,素火腿," +
            "桂林腐乳,豆腐丝,素鸡,素什锦,素大肠,薰干,酱豆腐,香干,豆腐干,上海南乳,菜干,腐乳(白),臭豆腐,北豆腐,酸豆乳,南豆腐,豆奶,豆浆,豆腐脑";

    public static final String data1 = "612/100,531/100,517/100,490/100,472/100,399/100,386/100,385/100,367/100,358/100,357/100,351/100,351/100,350/100,350/100," +
            "349/100,348/100,348/100,347/100,347/100,347/100,346/100,346/100,345/100,344/100,344/100,343/100,343/100,342/100,340/100,340/100,576/100,489/100," +
            "459/100,422/100,418/100,409/100,405/100,400/100,381/100,359/100,342/93,336/100,334/100,330/100,316/100,316/100,314/100,313/100,309/100,306/100," +
            "304/100,296/100,244/100,612/100,337/100,337/100,336/100,336/100,335/100,335/100,333/100,333/100,307/100,304/100,302/100,285/100,280/100,255/100," +
            "233/100,220/100,217/100,208/100,140/100,121/100,117/100,114/100,109/100,106/46,104/86,99/90,64/100,46/100,46/100,243/100,240/100,211/100,204/100," +
            "201/100,192/100,173/100,153/100,153/100,151/100,147/100,140/100,138/100,136/100,133/100,130/100,98/100,67/100,57/100,30/100,13/100,10/100";

    //蔬菜类
    public static final String name2 = "干姜,蕨菜(脱水),竹笋(黑笋，干),辣椒(红尖，干),黄花菜,竹笋(白笋，干),紫皮大蒜,大蒜,毛豆,豌豆,蚕豆,慈姑,番茄酱(罐头),芋头,土豆,甜菜,藕,苜蓿,荸荠," +
            "山药,香椿,枸杞菜,黄豆芽,胡萝卜(黄),玉兰片,鲜姜,洋葱,胡萝卜(红),扁豆,蒜苗,羊角豆,榆钱,苦菜,刀豆,芥菜头,西兰花(绿菜花),辣椒(红小),香菜,苋菜(紫),芹菜叶,青萝卜,苤蓝,大葱(鲜)," +
            "冬寒菜,豆角,白豆角,青蒜,豇豆,豇豆(长),豌豆苗,红菜苔,四季豆,荷兰豆,蓟菜,木瓜,韭菜,变萝卜,白菜苔,茭笋,芸豆,茄子(绿皮)," +
            "苋菜(青),雪里红,小葱,菠菜,菜花,茴香,小叶芥菜,茭白,油菜,辣椒(青，尖),南瓜,柿子椒,圆白菜,韭黄,油豆角,毛竹笋,心里美萝卜,蒜黄,茼蒿,番茄罐头(整),茄子,丝瓜,空心菜,萝卜樱(小，红)," +
            "木耳菜,白萝卜,油菜苔,竹笋(春笋),芹菜,芥蓝,小水萝卜,竹笋,西红柿,长茄子,苦瓜,菜瓜,西葫芦,芦笋,莴笋叶,绿豆芽,西洋菜(豆瓣菜),黄瓜,小白菜,牛俐生菜,大白菜(青白口),大白菜(酸菜)," +
            "大白菜(小白口),大叶芥菜(盖菜),旱芹,萝卜樱(白),莴笋,葫芦,水芹,生菜,减肥笋瓜,冬瓜,竹笋(鞭笋),面西胡瓜";

    public static final String data2 = "273/95,251/100,213/76,212/88,199/98,196/64,136/89,126/85,123/53,105/42,104/31,94/89,81/100,79/84,76/94,75/90,70/88,60/100,59/78,56/83," +
            "47/76,44/49,44/100,43/97,43/100,41/95,39/90,37/96,37/91,37/82,37/88,36/100,35/100,35/92,33/83,33/83,32/80,31/81,31/73,31/100,31/95,30/78,30/82,30/58,30/96,30/97," +
            "30/84,29/97,29/98,29/98,29/52,28/96,27/88,27/88,27/86,26/90,26/94,25/84,25/77,25/96,25/90,25/74,24/94,24/73,24/89,24/82,24/86,24/88,23/74,23/87,23/84,22/85,22/82," +
            "22/86,22/88,22/99,21/67,21/88,21/97,21/82,21/100,21/93,20/83,20/76,20/93,20/76,20/95,20/93,20/66,20/67,19/78,19/66,19/63,19/97," +
            "19/96,19/81,18/88,18/73,18/90,18/89,18/100,17/73,15/92,15/81,15/81,15/83,14/100,14/85,14/71,14/66,14/100,14/62,14/87,13/60,13/94,12/91,11/80,11/45,10/88";

    //水果类
    public static final String name3 = "松子仁,松子(生),核桃(干),松子(炒),葵花子(炒),葵花子仁,山核桃(干),葵花子(生),榛子(炒),花生(炒),花生仁(炒),南瓜子(炒)," +
            "西瓜子(炒),南瓜子仁,花生仁(生),西瓜子仁,榛子(干),杏仁,白果,栗子(干),莲子(干),葡萄干,苹果脯,杏脯,核桃(鲜),金丝小枣,果丹皮,无核蜜枣,桂圆肉," +
            "桃脯,西瓜脯,大枣(干),花生(生),杏酱,海棠脯,苹果酱,桂圆干,桃酱,草莓酱,干枣,柿饼,椰子,乌枣,黑枣,密云小枣,莲子(糖水),沙枣,栗子(鲜),红果(干),酒枣,鲜枣," +
            "芭蕉,红果,香蕉,人参果,海棠,柿子,桂圆(鲜),荔枝(鲜)离枝,甘蔗汁,玛瑙石榴,青皮石榴,无花果,红元帅苹果,桃罐头,红星苹果,猕猴桃,黄元帅苹果,金橘,京白梨,国光苹果,桃(黄桃),海棠罐头," +
            "倭锦苹果,鸭广梨,葡萄(巨峰),葡萄(玫瑰香),桑葚,青香蕉苹果,红香蕉苹果,黄香蕉苹果,橄榄,莱阳梨,苹果梨,紫酥梨,冬果梨罐头,橙子,巴梨,祝光苹果,桃(旱久保),樱桃,红富士苹果,伏苹果,福橘," +
            "印度苹果,红玉苹果,酥梨,鸭梨,芦柑,葡萄(紫),桃(五月鲜),蜜橘,菠萝,雪花梨,番石榴,桃(久保),蜜桃,柚子(文旦),四川红橘,苹果罐头,枇杷,小叶橘,冬果梨,杏子罐头,杏,李子,柠檬,李子杏,哈密瓜," +
            "西瓜(京欣一号),糖水梨罐头,芒果,草莓,红肖梨,杨桃,杨梅,库尔勒梨,柠檬汁,香瓜,西瓜(郑州三号),白兰瓜";

    public static final String data3 = "698/100,640/32,627/43,619/31,616/52,606/100,601/24,597/50,594/21,589/71,581/100,574/68,573/43,566/100," +
            "563/100,555/100,542/27,514/100,355/100,345/73,344/100,341/100,336/100,329/100,327/43,322/81,321/100,320/100,313/100,310/100,305/100," +
            "298/88,298/53,286/100,286/100,277/100,273/37,273/100,269/100,264/80,250/97,231/33,228/59,228/98,214/92,201/100,200/41,185/80,152/100,145/91," +
            "122/87,109/68,95/76,91/59,80/88,73/86,71/87,70/50,70/73,64/100,63/57,61/55,59/100,59/84,58/100,57/85,56/83,55/80,55/100,54/79,54/78,54/93,53/100," +
            "50/86,50/76,50/84,50/86,49/100,49/80,49/87,49/88,49/80,49/80,48/94,47/59,47/100,47/74,46/79,46/86,46/89,46/80,45/85,45/86,45/67,44/90,43/84,43/72," +
            "43/82,43/77,43/88,42/93,42/76,41/68,41/86,41/97,41/94,41/88,41/69,40/78,39/100,39/62,38/81,37/87,37/100,36/91,36/91,35/66,35/92,34/71,34/59,33/100," +
            "32/60,30/97,30/87,29/88,28/82,28/91,26/100,26/78,25/59,21/55";

    //肉类
    public static final String name4 = "猪肉(肥),羊肉干(绵羊),腊肠,猪肉(血脖),猪肉(肋条肉),牛肉干,酱汁肉,鸭皮,香肠,母麻鸭,牛肉松,鸡肉松,北京烤鸭,广东香肠," +
            "北京填鸭,瓦罐鸡汤(汤),猪肉松,猪肉(肥，瘦),肉鸡,咸肉,公麻鸭,猪肉(软五花),猪肉(硬五花),猪肉(前蹄膀),宫爆肉丁(罐头),猪肉(后臀尖),茶肠,猪肉(后蹄膀),金华火腿," +
            "猪肘棒(熟),盐水鸭(熟),蒜肠,小泥肠,羊肉(冻，山羊),猪肉香肠罐头,烧鹅,羊肉(冻，绵羊),风干肠,小红肠,叉烧肉,肯德基炸鸡,蛋清肠,猪排骨,大肉肠,酱羊肉,大腊肠,酱鸭,猪蹄,猪大排," +
            "午餐肠,红果肠,猪蹄(熟),母鸡(一年内鸡),鸡爪,驴肉(熟),酱鸭(罐头),猪肘棒,腊羊肉,酱牛肉,鹅,鸭舌,烤鸡,鸭,羊肉串(电烤),猪口条,午餐肉,小肚,羊舌,羊肉串(炸),羊肉(熟),扒鸡,火腿肠," +
            "卤煮鸡,猪肝(卤煮),鸽,猪肉(清蒸)," +
            "羊肉(肥，瘦),牛舌,鸡翅,猪大肠,猪耳,猪肉(腿),瓦罐鸡汤(肉),卤猪杂,腊肉,鸡腿,羊蹄筋(生),鸡心,煨牛肉(罐头),酱驴肉,猪蹄筋,猪肉(里脊),牛蹄筋,鸭掌," +
            "牛蹄筋(熟),沙鸡,鸭翅,鸭心,火鸡肝,猪肉(瘦),羊脑,牛肝,乌鸦肉,羊肝,鸡胸脯肉,猪脑,猪肝,鹅肝,喜鹊肉,鸭肝,土鸡,马肉,鸡肝(肉鸡),鸡肝,猪心,羊肉(瘦),鸡胗,方腿,狗肉,驴肉(瘦)," +
            "羊心,羊肉(前腿),乌骨鸡,鹌鹑,猪肚,羊肉(胸脯),羊肉(颈),牛肉(瘦),火鸡胸脯肉,羊肉(后腿),兔肉,牛肉(前腱),鹅肫,牛肉(后腿),猪腰子,牛肉(前腿),牛肺,羊肉(脊背),牛肉(后腱),鸭肫," +
            "火鸡肫,火鸡腿,羊肾,鸭胸脯肉,羊肚,野兔肉,猪肺,牛肚,羊大肠,猪小肠,鸭血(白鸭),羊血,猪血,鸡血";

    public static final String data4 = "816/100,588/100,584/100,576/90,568/96,550/100,549/96,538/100,508/100,461/75,445/100,440/100,436/80,433/100," +
            "424/75,408/100,396/100,395/100,389/74,385/100,360/63,349/85,339/79,338/67,336/100,331/97,329/100,320/73,318/100,314/72,312/81,297/100," +
            "295/100,293/100,290/100,289/73,285/100,283/100,280/100,279/100,279/70,278/100,278/72,272/100,272/100,267/100,266/80,266/60,264/68,261/100," +
            "260/100,260/43,256/66,254/60,251/100,248/93,248/67,246/100,246/100,245/63,245/61,240/73,240/68,234/100,233/94,229/100,225/100,225/100,217/100," +
            "215/100,215/66,212/100,212/70,203/100,201/42,198/100,198/90,196/100,194/69,191/100,190/100,190/100,190/100,186/100,181/100,181/69,177/100,172/100," +
            "166/100,160/100,156/100,155/100,151/100,150/59,147/100,147/41,146/67,143/100,143/100,143/100,142/100,139/100,136/100,134/100,133/100,131/100,129/99," +
            "129/100,128/100,128/100,124/58,122/100,121/100,121/100,119/97,118/90,118/100,117/100,116/80,116/100,113/100,111/71,111/48,110/58,110/96,109/81,109/74," +
            "106/100,103/100,102/77,102/100,100/95,100/100,98/100,96/93,95/100," +
            "94/100,94/100,93/94,92/93,91/100,90/100,90/100,90/100,87/100,84/100,84/97,72/100,70/100,65/100,58/100,57/100,55/100,49/100";

    //蛋类
    public static final String name5 = "蛋黄粉,鸡蛋粉,鸭蛋黄,鸡蛋黄,鹅蛋黄,鹅蛋,咸鸭蛋,鸭蛋,松花蛋(鸡),松花蛋(鸭),鹌鹑蛋,鸡蛋(红皮),鹌鹑蛋(五香罐头),鸡蛋(白皮),鸡蛋白,鹅蛋白,鸭蛋白";

    public static final String data5 = "644/100,545/100,378/100,328/100,324/100,196/87,190/88,180/87,178/83,171/90,160/86,156/88,152/89,138/87,60/100,48/100,47/100";

    //水产类
    public static final String name6 = "鲮鱼(罐头),淡菜(干),蛏干,鲍鱼(干),鱿鱼(干),鱼片干,墨鱼(干),干贝,海参,鱼子酱(大麻哈),海鲫鱼,丁香鱼(干),海米,堤鱼,河鳗,腭针鱼,香海螺,快鱼," +
            "鲐鱼,虾皮,白姑鱼,胡子鲇,大麻哈鱼,平鱼,尖嘴白,鳊鱼(武昌鱼),八爪鱼,口头鱼,黄姑鱼,带鱼,黄鳍鱼,鲚鱼(小凤尾鱼),边鱼,沙梭鱼,海鳗,鲅鱼,银鱼,红螺,桂鱼,青鱼,赤眼鳟(金目鱼),梅童鱼," +
            "草鱼,鲨鱼,鲤鱼,鲫鱼,比目鱼,鲷(加吉鱼)," +
            "鲚鱼(大凤尾鱼),片口鱼,河蟹,鲇鱼,鲢鱼,基围虾,金线鱼,狗母鱼,鲈鱼,鳙鱼(胖头鱼),小黄花鱼,红鳟鱼,罗非鱼,蛤蜊(毛蛤蜊),泥鳅,大黄鱼,鲮鱼,海蟹,梭子蟹,螯虾,对虾,龙虾,黄鳝(鳝鱼)," +
            "沙丁鱼,明太鱼,石斑鱼,明虾,河虾,乌贼,麦穗鱼,鲍鱼,面包鱼,墨鱼,琵琶虾,淡菜(鲜),海虾,鲜贝,非洲黑鲫鱼,鱿鱼(水浸),海蛰头,牡蛎,蚶子,海参(鲜),蚌肉,海蛎肉,乌鱼蛋,蟹肉,鲜赤贝," +
            "黄鳝(鳝丝),鲜扇贝,田螺,生蚝,蛤蜊(沙蛤),章鱼,河蚬,蛤蜊(花蛤),蛏子,河蚌,海蛰皮,海参(水浸)";

    public static final String data6 = "399/100,355/100,340/100,322/100,313/98,303/100,287/82,264/100,262/93,252/100,206/60,196/100,195/100,191/64,181/84,180/75," +
            "163/59,159/71,155/66,153/100,150/67,146/50,143/72,142/70,137/80,135/59,135/78,134/56,133/63,127/76,124/52,124/90,124/70,122/72,122/67,122/80,119/100,119/55," +
            "117/61,116/63,114/59,113/63,112/58,110/56,109/54,108/54,107/72,106/65,106/79,105/68,103/42,102/65,102/61,101/60,100/40,100/67,100/58,100/61,99/63,99/57,98/55," +
            "97/25,96/60,96/66,95/57,95/55,95/49,93/31,93/61,90/46,89/67,88/67,88/45,85/57,85/57,84/86,84/97,84/63,84/65,83/52,82/69,81/32,80/49,79/51,77/100,77/53,75/98," +
            "74/100,73/100,71/27,71/100,71/63,66/100,66/73,62/100,61/34,61/88,60/35,60/26,57/100,56/50,52/100,47/35,45/46,40/57,36/23,33/100,24/100";

    //奶类
    public static final String name7 = "黄油,奶油,黄油渣,牛奶粉(母乳化奶粉),羊奶粉(全脂),牛奶粉(强化维生素),牛奶粉(全脂),奶片,牛奶粉(全脂速溶),奶皮子,牛奶粉(婴儿奶粉),奶疙瘩," +
            "冰淇淋粉,奶豆腐(脱脂),炼乳(罐头，甜),奶酪,奶豆腐(鲜),酸奶,果料酸奶,母乳,酸奶(中脂),酸奶(高蛋白),羊奶(鲜),脱脂酸奶,牛奶,牛奶(强化VA，VD),酸奶(橘味脱脂),果味奶";

    public static final String data7 = "892/100,720/100,599/100,510/100,498/100,484/100,478/100,472/100,466/100,460/100,443/100,426/100,396/100,343/100,332/100,328/100," +
            "305/100,72/100,67/100,65/100,64/100,62/100,59/100,57/100,54/100,51/100,48/100,20/100";

    //油脂类
    public static final String name8 = "棕榈油,菜籽油,茶油,豆油,花生油,葵花籽油,棉籽油,牛油(炼),色拉油,香油,猪油(炼),鸭油(炼),大麻油,羊油(炼),玉米油,牛油,猪油(未炼),羊油,辣椒油,胡麻油";

    public static final String data8 = "900/100,899/100,899/100,899/100,899/100,899/100,899/100,898/100,898/100,898/100,897/100,897/100,897/100,895/100," +
            "895/100,835/100,827/100,824/100,450/100,450/100";

    //糕点小吃
    public static final String name9 = "VC饼干,曲奇饼,焦圈,维夫饼干,麻花,开口笑,凤尾酥,起酥,京式黄酥,桃酥,核桃薄脆,福来酥,春卷,硬皮糕点,鹅油卷,混糖糕点,蛋麻脆,开花豆," +
            "钙奶饼干,月饼(奶油果馅),江米条,月饼(奶油松仁),鸡腿酥,黑麻香酥,京八件,状元饼,奶油饼干,饼干(奶油),月饼(百寿宴点),酥皮糕点,月饼(枣泥),黑洋酥,月饼(五仁),苏打饼干," +
            "香油炒面,月饼(豆沙),麻香糕," +
            "麻烘糕,菠萝豆,蛋黄酥,蛋糕(奶油),面包(法式牛角),藕粉,美味香酥卷,蜜麻花,绿豆糕,蛋糕,桂花藕粉,蛋糕(蛋清),茯苓夹饼,碗糕,面包(黄油),烧饼,面包(椰圈),蛋糕(蒸)," +
            "面包(多维),面包,栗羊羹,面包(法式配餐),炸糕,面包(维生素),面包(果料),面包(咸),面包(麦胚),三鲜豆皮,烧麦,汤包,驴打滚,白水羊头,艾窝窝,爱窝窝,年糕,灌肠,豌豆黄," +
            "炒肝,油茶,茶汤,小豆粥,凉粉(带调料),豆腐脑(带卤),凉粉,豆汁(生)";

    public static final String data9 = "572/100,546/100,544/100,528/100,524/100,512/100,511/100,499/100,490/100,481/100,480/100,465/100,463/100," +
            "463/100,461/100,453/100,452/100,446/100,444/100,441/100,439/100,438/100,436/100,436/100,435/100,435/100,429/100,429/100,428/100,426/100," +
            "424/100,417/100,416/100,408/100,407/100,405/100,401/100,397/100,392/100,386/100,378/100,375/100,372/100,368/100,367/100,349/100,347/100," +
            "344/100,339/100,332/100,332/100,329/100,326/100,320/100,320/100,318/100,312/100,301/100,282/100,280/100,279/100,278/100,274/100,246/100,240/100,238/100,238/100," +
            "194/100,193/100,190/100,190/100,154/100,134/100,133/100,96/100,94/100,92/100,61/100,50/100,47/100,37/100,10/100";

    //糖类
    public static final String name10 = "巧克力,巧克力(维夫),芝麻南糖,酥糖,奶糖,巧克力(酒芯),酸三色糖,冰糖,绵白糖,红糖,米花糖,泡泡糖,淀粉(团粉),淀粉(玉米),淀粉(土豆粉),蜂蜜";

    public static final String data10 = "586/100,572/100,538/100,436/100,407/100,400/100,397/100,397/100,396/100,389/100,384/100,360/68,346/100,345/100,337/100,321/100";

    //饮料类
    public static final String name11 = "麦乳精,酸梅精,山楂精,二锅头(58度),可可粉,甲级龙井,铁观音,绿茶,红茶,花茶,橘汁(浓缩蜜橘),紫雪糕,砖茶,冰砖,冰淇淋,橘子汁," +
            "红葡萄酒(16度),红葡萄酒(12度),白葡萄酒(11度),喜乐,冰棍,杏仁露,汽水(特制),巧克力豆奶,柠檬汽水,北京6度特制啤酒";

    public static final String data11 = "429/100,394/100,386/100,352/100,320/100,309/100,304/100,296/100,294/100,281/100,235/100,228/100,206/100,153/100,126/100," +
            "119/100,91/100,68/100,62/100,53/100,47/100,46/100,42/100,39/100,38/100,35/100";

    //茵藻类
    public static final String name12 = "石花菜,琼脂,发菜,口蘑,普中红蘑,珍珠白蘑,冬菇,香菇(干),杏丁蘑,紫菜,黑木耳,大红菇,白木耳,黄蘑,榛蘑,苔菜,松蘑,海带(干),金针菇,草菇," +
            "双孢蘑菇,水发木耳,金针菇(罐装),平菇,鲜蘑,香菇(鲜),海带(鲜),猴头菇(罐装)";

    public static final String data12 = "314/100,311/100,246/100,242/100,214/100,212/100,212/86,211/95,207/100,207/100,205/100,200/100,200/96,166/89,157/77,148/100,112/100," +
            "77/98,26/100,23/100,22/97,21/100,21/100,20/93,20/99,19/100,17/100,13/100";

    //其它食品
    public static final String name13 = "芝麻酱,花生酱,芥末,胡椒粉,味精,豆鼓(五香),辣油豆瓣酱,豆瓣酱,甜面酱,辣酱(麻),黄酱,醋,牛肉辣瓣酱,糖蒜,甜辣黄瓜,郫县辣酱,合锦菜," +
            "八宝菜(酱),酱油,萝卜干,豆瓣辣酱,大头菜(桂花),冬菜,酱苤蓝丝,芥菜头,辣萝卜条,大头菜(酱),辣椒糊,酱萝卜,榨菜,腌雪里红,酱黄瓜,韭菜花(腌)";

    public static final String data13 = "618/100,594/100,476/100,357/100,268/100,244/100,184/100,178/100,136/100,135/100,131/100,130/100,127/100,114/74,99/100,89/100,75/100,72/100," +
            "71/100,60/100,59/100,51/100,46/100,39/100,38/100,37/100,36/100,31/100,30/100,29/100,25/100,24/100,15/100";

}
