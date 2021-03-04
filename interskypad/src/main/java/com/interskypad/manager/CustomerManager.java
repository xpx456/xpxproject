package com.interskypad.manager;

import com.interskypad.database.DBHelper;
import com.interskypad.entity.Customer;
import com.interskypad.view.InterskyPadApplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import intersky.apputils.CharacterParser;

public class CustomerManager {


    public static boolean[] typebooleans3 = new boolean[26];
    public ArrayList<Customer> customers = new ArrayList<Customer>();
    public ArrayList<Customer> allcustomers = new ArrayList<Customer>();
    public ArrayList<Customer> customersHead = new ArrayList<Customer>();
    public ArrayList<Customer> mSearchItems = new ArrayList<Customer>();
    public ArrayList<Customer> mSearchHeadItems = new ArrayList<Customer>();
    public HashMap<String,Customer> hashCustomer= new HashMap<String,Customer>();
    public static CustomerManager mCustomerManager;

    public static synchronized CustomerManager getInstance() {
        if (mCustomerManager == null) {
            mCustomerManager = new CustomerManager();
        }
        return mCustomerManager;
    }

    public CustomerManager() {
        customers.addAll(DBHelper.getInstance(InterskyPadApplication.mApp).scanCustomer(hashCustomer));
        typebooleans3 = new boolean[26];
        for (int i = 0; i < customers.size(); i++) {
            Customer mContactModel2 = customers.get(i);
            String s = mContactModel2.mPingyin.substring(0, 1).toUpperCase();
            int pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (typebooleans3[pos] == false) {
                    customersHead.add(new Customer(s));
                    typebooleans3[pos] = true;
                }
            }
        }
        allcustomers.addAll(customers);
        allcustomers.addAll(0, customersHead);
        Collections.sort(allcustomers, new SortContactComparator());
        Collections.sort(customersHead, new SortContactComparator());
    }

    public static class SortContactComparator implements Comparator {

        @Override
        public int compare(Object mMailPersonItem1, Object mMailPersonItem2) {
            // TODO Auto-generated method stub
            String[] array = new String[2];

            array[0] = ((Customer) mMailPersonItem1).mPingyin;
            array[1] = ((Customer) mMailPersonItem2).mPingyin;
            if (array[0].equals(array[1])) {
                return 0;
            }
            Arrays.sort(array);
            if (array[0].equals(((Customer) mMailPersonItem1).mPingyin)) {
                return -1;
            } else if (array[0].equals(((Customer) mMailPersonItem2).mPingyin)) {
                return 1;
            }
            return 0;
        }
    }

    public void addCustomer(Customer customer) {
        Customer mContactModel2 = customer;
        if(!hashCustomer.containsKey(mContactModel2.getName()))
        {
            mCustomerManager.customers.add(customer);
            mCustomerManager.allcustomers.add(customer);
        }
        String s = mContactModel2.mPingyin.substring(0, 1).toUpperCase();
        int pos = CharacterParser.typeLetter.indexOf(s);
        if (pos != -1) {
            if (typebooleans3[pos] == false) {
                Customer head = new Customer(s);
                mCustomerManager.customersHead.add(head);
                typebooleans3[pos] = true;
                mCustomerManager.allcustomers.add(0, head);
            }
        }
        Collections.sort(mCustomerManager.allcustomers, new SortContactComparator());
        Collections.sort(mCustomerManager.customersHead, new SortContactComparator());
        mCustomerManager.hashCustomer.put(customer.getName(),customer);
    }

    public void removeCustomer(Customer customer) {
        Customer mContactModel2 = mCustomerManager.hashCustomer.get(customer.getName());
        String s = mContactModel2.mPingyin.substring(0, 1).toUpperCase();
        int a = mCustomerManager.allcustomers.indexOf(customer);
        if(a == mCustomerManager.allcustomers.size()-1 || !mCustomerManager.allcustomers.get(a+1).mPingyin.substring(0, 1).toLowerCase().equals(s))
        {
            int pos = CharacterParser.typeLetter.indexOf(s);
            if (pos != -1) {
                if (typebooleans3[pos] == true) {
                    Customer head = null;
                    for(int i = 0 ; i < mCustomerManager.customersHead.size() ; i++)
                    {
                        if(mCustomerManager.customersHead.get(i).getName().equals(s))
                        {
                            head = mCustomerManager.customersHead.get(i);
                            break;
                        }
                    }
                    if(head != null)
                    {
                        mCustomerManager.customersHead.remove(head);
                        mCustomerManager.allcustomers.remove(head);
                    }
                    typebooleans3[pos] = false;
                }
            }
        }
        allcustomers.remove(mContactModel2);
        customers.remove(mContactModel2);
        Collections.sort(mCustomerManager.allcustomers, new SortContactComparator());
        Collections.sort(mCustomerManager.customersHead, new SortContactComparator());
        mCustomerManager.hashCustomer.remove(customer.getName());
    }

    public ArrayList<String> getCustomData() {
        ArrayList<String> strings = new ArrayList<String>();
        for(int i = 0 ; i < customers.size() ; i++)
        {
            strings.add(customers.get(i).getName());
        }
        return strings;
    }
}
