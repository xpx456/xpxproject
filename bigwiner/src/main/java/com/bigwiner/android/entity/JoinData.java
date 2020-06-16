package com.bigwiner.android.entity;

import java.util.ArrayList;
import java.util.HashMap;

import intersky.select.entity.MapSelect;
import intersky.select.entity.Select;

public class JoinData {

    public MapSelect ports = new MapSelect();
    public MapSelect businessareaSelect = new MapSelect();
    public MapSelect businesstypeSelect = new MapSelect();
    public Select positions = new Select("","");
//    public Select ports = new Select("","");
//    public Select routes = new Select("","");
//    public Select serices = new Select("","");
}
