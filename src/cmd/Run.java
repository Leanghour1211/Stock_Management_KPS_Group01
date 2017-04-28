/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmd;

import java.util.*;
import java.io.*;
import code.*;
/**
 *
 * @author User
 */
public class Run {
    public Run()
    {
       System.out.println("Choice is : "+ getMenu()); 
    }
    public static void main(String[] args) {
        new Run();
    }
    public void LoadHeader(FileReader file)
    {
        
    }
    public ArrayList<Products> getAllProduct()
    {
        ArrayList<Products> data=new ArrayList<Products>();
    
        return data;
    }
    public void Display(ArrayList<Products>data,int row,long currentpage)
    {
        
    }
    public void Update(ArrayList<Products>data,Products product)
    {
    
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
    public String getMenu() 
    {
        String tmp,result="";
        String[] list={"*","w","r","u","d","f","p","n","l","s","g","se","sa","b","re","h","e"};
        boolean isOk=false;
        do
        {
            Scanner read=new Scanner(System.in);
            System.out.print("--> OPTION : ");
            tmp=read.nextLine();
            for(int i=0;i<list.length;i++)
            {
                if(tmp.trim().compareToIgnoreCase(list[i])==0)
                {
                    result=list[i];
                    isOk=true;
                    break;
                }
            }
            if(!isOk)
            {
                System.out.println("Invalid Choice!!!");
            }
        }while(!isOk);
        return result;
    }
    public long getLastID(ArrayList<Products>data)
    {
        return data.get(data.size()-1).getID()+1;
    }
    public String getStringName()
    {
        boolean isOk=false;
        String tmp="";
        do
        {
            Scanner read=new Scanner(System.in);
            System.out.print("Input new Name : ");
            tmp=read.nextLine();
            for(char k : tmp.toCharArray())
            {
                if(Character.isLetter(k)||k=='\''||k==' '||k=='.'||k=='_'||k=='-')
                {
                
                }
            
                
            }
            
        }while(!isOk);
        return tmp;
    }
}
