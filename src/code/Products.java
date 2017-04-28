/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package code;

import java.io.Serializable;
import java.util.*;

/**
 *
 * @author User
 */
public class Products implements Serializable,Comparable<Products> {
    private long ID;
    private String Name;
    private float UnitPrice;
    private int StockQty;
    private Date ImportDate;
    
    public Products(long ID, String Name, float UnitPrice, int StockQty, Date ImportDate) {
        setName(Name);
        setID(ID);
        setStockQty(StockQty);
        setUnitPrice(UnitPrice);
        setImportDate(ImportDate);
    }
    

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name =MaskName(Name);
    }

    public float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(float UnitPrice) {
        if(UnitPrice<0)UnitPrice=-UnitPrice;
        this.UnitPrice = UnitPrice;
    }

    public int getStockQty() {
        return StockQty;
    }

    public void setStockQty(int StockQty) {
        if(StockQty<0)StockQty=-StockQty;
        this.StockQty = StockQty;
    }

    public Date getImportDate() {
        return ImportDate;
    }

    public void setImportDate(Date ImportDate) {
        this.ImportDate = ImportDate;
    }
    public String MaskName(String text)
    {
    String tmp="";
        for(char k : text.toCharArray())
        {
            if(Character.isLetter(k)||k=='\''||k==' '||k=='.'||k=='_'||k=='-')
            tmp+=k;
        }
    return tmp;
    }
    public String toString()
    {
    return "";
    }

    @Override
    public int compareTo(Products o) {
        //compare by ID
    if(o.getID()<this.getID())return 1;
    if(o.getID()>this.getID())return -1;
    return 0;
    }
    public boolean Equal(Products o)
    {
        //true is ID is Equal
        return (compareTo(o)==0);
    }
    
}
