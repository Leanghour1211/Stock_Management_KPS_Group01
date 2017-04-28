/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmd;

import java.util.*;
import java.io.*;
import code.*;
import java.text.SimpleDateFormat;
import java.util.logging.*;
import java.text.*;
/**
 *
 * @author User
 */
public class Run {
    public Run()
    {
        boolean isExit=false;
        do
        {
            DisplayMenu();
            switch(getMenu())
            {
                case "*": break;
                case "w": 
                        {
                            write();
                        }
                            break;
                case "r":
                        {
                            read();
                        }
                            break;
                case "u":
                        {
                            Update();
                        }
                            break;
                case "d": break;
                case "f": break;
                case "p": break;
                case "n": break;
                case "l": break;
                case "s": 
                        {
                            Search();
                        }
                            break;
                case "g": break;
                case "se": break;
                case "sa":
                        {
                            ArrayList<Products>tmp=ReadFromFile("Data\\last.bin");
                            if(tmp.isEmpty())
                            {
                                System.out.println("No data to save!!!");break;
                            }
                            if(isYes("Save Last Data?"))
                            {
                                SaveData();
                            }
                        }   
                            break;
                case "b": break;
                case "re": break;
                case "h": break;
                case "e":isExit=isYes("Are You Sure Want to Exit?"); break;
            }
        }while(!isExit);
    }
    public static void main(String[] args) {
        new Run();
    }
    public void write()
    {
        ArrayList<Products>tmp=new ArrayList<Products>();
        Products pro=getProduct();
        if(pro == null)return;
        tmp.add(pro);
        //System.out.println(pro.toString());
        SaveToFile(tmp, "Data\\last.bin");
        System.out.println(pro.toString());
        if(isYes("Save Data Now? "))
        {
            SaveData();
        }
    }
    public void Update()
    {
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        SaveToFile(data, "Data\\lastbackup.bin");
        if(data.isEmpty())
        {
        System.out.println("No any data");
        return;
        }
        long id=getID();
        long index=getIndex(data, id);
        if(index==-1)
        {
            System.out.println("Not Found!!!");
            return;
        }
        Products pro=data.get((int)index);
        System.out.println("Product Name : "+pro.getName());
        if(isYes("Edit Name?"))
        pro.setName(getProductName("new"));
        System.out.println("Product Qty : "+pro.getStockQty());
        if(isYes("Edit Qty?"))
        pro.setStockQty(getStockQty("new"));
        System.out.println("Unit Price : "+pro.getName());
        if(isYes("Edit Price?"))
        pro.setUnitPrice(getUnitPrice("new"));
        pro.setImportDate(new Date());
        data.set((int)index, pro);
        if(isYes("Update Information?"))
        {
            SaveToFile(data, "Data\\Products.bin");
            System.out.println("Update SuccessFully!!!");
        }
        else
            System.out.println("Update Canceled!!!");
        
    }
    public long getIndex(ArrayList<Products>data,long ID)
    {
        for(int i=0;i<data.size();i++)
        {
            if(data.get(i).getID()==ID)
            {
                return i;
            }
        }
        return -1;
    }
    public void SaveData()
    {
        ArrayList<Products>tmp=ReadFromFile("Data\\last.bin");
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        
        if(tmp.isEmpty())
        {
            System.out.println("No Last Data to Save!!!");
            return;
        }
        data.add(tmp.get(0));
        SaveToFile(data, "Data\\Products.bin");
        tmp.clear();
        SaveToFile(tmp, "Data\\last.bin");
        System.out.println("Save SuccessFully!!!");
    }
    public Products getProduct()
    {   
        DecimalFormat fmt=new DecimalFormat("00000000");
        long id=getLastID(ReadFromFile("Data\\Products.bin"));
        System.out.println("New ID : "+fmt.format(id));
        String Name="";
        float price=0;
        int qty=0;
        Date date=new Date();
        Name=getProductName("");
        if(Name.equalsIgnoreCase("exit"))return null;
        price=getUnitPrice("");
        if(price==-1)return null;
        qty=getStockQty("");
        if(qty==-1)return null;
        return new Products(id,Name,price,qty,date);
    }
    public Products getProduct(ArrayList<Products>data,long id)
    {
        for(Products p:data)
        {
            if(p.getID()==id)
            {
                return p;
            }
        }
        return null;
    }
    public void SaveToFile(ArrayList<Products>data,String FilePath)
    {
        try {
            ObjectOutputStream write=new ObjectOutputStream(new FileOutputStream(FilePath));
            write.writeObject(data);
            write.close();
        } catch (IOException ex) {
        
        }
    }
    public ArrayList<Products> ReadFromFile(String FilePath)
    {
        ArrayList<Products> data=null;
        try {
            ObjectInputStream read=new ObjectInputStream(new FileInputStream(FilePath));
            data=(ArrayList<Products>)read.readObject();
            read.close();
        } catch (IOException ex) {
            try {
                ObjectOutputStream write=new ObjectOutputStream(new FileOutputStream(FilePath));
                write.writeObject(new ArrayList<Products>());
                write.close();
                return ReadFromFile(FilePath);
            } catch (IOException ex1) {
            
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Error While Converting Data");
        }
        return data;
    }
                      
    public void LoadHeader()
    {
        try {
            System.out.println(new FileReader("MainMenu.txt"));
        } catch (FileNotFoundException ex) {
        }
    }
    public String LoadStringFile(FileReader file)
    {
        String result="";
        try
        {
            int tmp=0;
            while((tmp=file.read())!=-1)
            {
                result+=(char)tmp;
            }
        }catch(IOException ex)
        {
        
        }
        return result;
    }
    public boolean isYes(String msg)
    {
        boolean isOk=false;
        String tmp="";
        do
        {
        Scanner read=new Scanner(System.in);
        System.out.print(msg+" [y/n] : ");
        tmp=read.nextLine();
        isOk=(tmp.equalsIgnoreCase("y"))?true:(tmp.equalsIgnoreCase("n"))?true:false;
        if(!isOk)System.out.println("Invalid");
        }while(!isOk);
        return (tmp.equalsIgnoreCase("y"));
    }
    public void Display(ArrayList<Products>data,int row,long currentpage)
    {
        
    }
   
    public String getProductName(String prefix)
    {
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input "+prefix+" Product Name : ");
            Scanner read=new Scanner(System.in);
            isOk=!(tmp=read.nextLine().trim()).equals("");
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
        return tmp;
    }
    public float getUnitPrice(String prefix)
    {
        float result=0;
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input "+prefix+" Product UnitPrice : ");
            Scanner read=new Scanner(System.in);
            tmp=read.nextLine().trim();
            try
            {
            result=Float.parseFloat(tmp);
            isOk=true;
            }catch(NumberFormatException ex)
            {
                if(tmp.equalsIgnoreCase("exit"))return -1;
            }
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
        if(result<0)result=-result;
        return result;
    }
    public void read()
    {
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
        {
            System.out.println("No data to read!!!");return;
        }
        long id= getID();
        if(id==-1)return;
        
        Products pro=null;
        if((pro=getProduct(data, id))==null)
        {
            System.out.println("Not Found!!!");
            return;
        }
        System.out.println(pro.toString());  
        System.out.print("Press Enter to Continue!!! : ");
        new Scanner(System.in).nextLine();
    }
    public ArrayList<Products> getListContainedX(String X)
    {
        ArrayList<Products>data=new ArrayList<Products>();
        for(Products p:ReadFromFile("Data\\Products.bin"))
        {
            if(p.isContain(X))data.add(p);
        }
        return data;
    }
    public void Search()
    {
        String name=getProductName("Search");
        if(name.equalsIgnoreCase("exit"))return;
        ArrayList<Products>data=getListContainedX(name);
        if(data.isEmpty())System.out.println("Not Found!!!");
            for(Products p:data)
            {
                System.out.println(p.toString());
            }
        System.out.println("Press Enter to Continue!");
        new Scanner(System.in).nextLine();
    }
    public int getStockQty(String prefix)
    {
        int result=0;
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input "+prefix+" Product Qty : ");
            Scanner read=new Scanner(System.in);
            tmp=read.nextLine().trim();
            try
            {
            result=Integer.parseInt(tmp);
            isOk=true;
            }catch(NumberFormatException ex)
            {
                if(tmp.equalsIgnoreCase("exit"))return -1;
            }
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
        if(result<0)result=-result;
        return result;
    }
    public long getID()
    {
        long result=0;
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input ID to Search : ");
            Scanner read=new Scanner(System.in);
            tmp=read.nextLine().trim();
            try
            {
            result=Long.parseLong(tmp);
            isOk=true;
            }catch(NumberFormatException ex)
            {
                if(tmp.equalsIgnoreCase("exit"))return -1;
            }
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
        if(result<0)result=-result;
        return result;
    }
    public boolean isExist(long ID)
    {
        //if the array have thr ID
        return true;
    }
    public long IndexOf(Products product)
    {
    
        return 0;
    }
    public void DisplayMenu()
    {
        try
        {
            System.out.println(LoadStringFile(new FileReader("MainMenu.txt")));
        }catch(IOException ex)
        {
        
        }
        
    }
    public String getMenu() 
    {
        String tmp,result="";
        String[] list={"*","w","r","u","d","f","p","n","l","s","g","se","sa","b","re","h","e"};
        boolean isOk=false;
        do
        {
            Scanner read=new Scanner(System.in);
            System.out.print("--> OPTION : ");
            tmp=read.nextLine().toLowerCase();
            for(int i=0;i<list.length;i++)
            {
                if(tmp.trim().compareToIgnoreCase(list[i])==0)
                {
                    result=list[i];
                    isOk=true;
                    break;
                }
            }
            if(!isOk)System.out.println("Invalid Choice!!!");
            
        }while(!isOk);
        return result;
    }
    public long getLastID(ArrayList<Products>data)
    {
        return (data.size()>0)?data.get(data.size()-1).getID()+1:1;
    }
    
}
