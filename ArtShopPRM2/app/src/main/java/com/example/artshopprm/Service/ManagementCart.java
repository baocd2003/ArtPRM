package com.example.artshopprm.Service;

import android.content.Context;
import android.widget.Toast;

import com.example.artshopprm.Entity.Account;
import com.example.artshopprm.Entity.Art;

import java.util.ArrayList;

public class ManagementCart {
    private Context context;
    private DbHelper dbHelper;

    public ManagementCart(Context context) {
        this.context = context;
        this.dbHelper=new DbHelper(context);
    }

    public void insertFood(Art item) {
        ArrayList<Art> listpop = getListCart();
        boolean existAlready = false;
        int n = 0;
        for (int i = 0; i < listpop.size(); i++) {
            if (listpop.get(i).getArtName().equals(item.getArtName())) {
                existAlready = true;
                n = i;
                break;
            }
        }
        if(existAlready){
            listpop.get(n).setNumberInCart(item.getNumberInCart()+1);
        }else{
            listpop.add(item);
        }
        dbHelper.putListObject("CartList",listpop);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    public void setUserLogin(Account acc){
        dbHelper.putUserData("User",acc);
    }

    public Account getUserLogined(String key){
        return dbHelper.getAccountLogin("User");
    }

    public ArrayList<Art> getListCart() {
        return dbHelper.getListObject("CartList");
    }
    public void emptyListCart() {
        dbHelper.remove("CartList");
    }

    public Double getTotalFee(){
        ArrayList<Art> listItem=getListCart();
        double fee=0;
        for (int i = 0; i < listItem.size(); i++) {
            fee=fee+(listItem.get(i).getPrice()*listItem.get(i).getNumberInCart());
        }
        return fee;
    }
    public void minusNumberItem(ArrayList<Art> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart()==1){
            listItem.remove(position);
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()-1);
        }
        dbHelper.putListObject("CartList",listItem);
        changeNumberItemsListener.change();
    }
    public  void plusNumberItem(ArrayList<Art> listItem,int position,ChangeNumberItemsListener changeNumberItemsListener){
        if(listItem.get(position).getNumberInCart() > listItem.get(position).getStockQuantity()){
            Toast.makeText(context, "Out of quantity", Toast.LENGTH_SHORT).show();
        }else{
            listItem.get(position).setNumberInCart(listItem.get(position).getNumberInCart()+1);
            dbHelper.putListObject("CartList",listItem);
            changeNumberItemsListener.change();
        }
    }
}
