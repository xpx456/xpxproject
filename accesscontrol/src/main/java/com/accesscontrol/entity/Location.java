package com.accesscontrol.entity;

import java.util.ArrayList;

public class Location {

    public String address = "";
    public String addressid = "";
    public String parentid = "";
    public boolean isopen = false;
    public ArrayList<Location> locations = new ArrayList<Location>();

}
