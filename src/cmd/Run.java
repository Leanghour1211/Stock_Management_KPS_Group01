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
import org.nocrala.tools.texttablefmt.*;

/**
 *
 * @author User
 */
public class Run {
    public Run()
    {
        boolean isExit=false;
        try{
                    System.out.println(LoadStringFile(new FileReader("Header.txt")));
                    }catch(IOException ex)
                    {
                    
                    }
        do
        {
            DisplayMenu();
            switch(getMenu())
            {
                case "*":
                    {
                        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
                        if(data.isEmpty())
                        {
                            System.out.println("No data to display!!!");
                            break;
                        }
                        Display(data,(int) getRow(), getCurrentPage());
                    }
                    break;
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
                case "d":
                        delete();
                    break;
                case "f":first();
                        break;
                case "p":pre();
                        break;
                case "n":
                        next();
                        break;
                case "l":last();
                        break;
                case "s": 
                        {
                            Search();
                        }
                            break;
                case "g":
                    {
                        System.out.println("Go to Page : ");
                        long page=getChoice(1,(int) getLastPage());
                        if(page==-1)break;
                        savePage(page);
                        Display(ReadFromFile("Data\\Products.bin"),(int) getRow(), getCurrentPage());
                    }   
                    break;
                case "se":
                        {
                        setRow();
                        getLastPage();
                        }
                    break;
                case "sa":
                        {
                            ArrayList<Products>tmp=ReadFromFile("Data\\last.bin");
                            if(tmp.isEmpty())
                            {
                                System.out.println("No data to save!!!");break;
                            }
                            System.out.println(tmp.get(0).toString());
                            if(isYes("Save Last Data?"))
                            {
                                SaveData();
                            }
                        }   
                            break;
                case "b":
                        BackUp();
                    break;
                case "re":
                    {
                    Restore();
                    }
                    break;
                case "h":
                    try{
                    System.out.println(LoadStringFile(new FileReader("help.txt")));
                    }catch(IOException ex)
                    {
                    
                    }
                    break;
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
        System.out.println("Unit Price : "+pro.getUnitPrice());
        if(isYes("Edit Price?"))
        pro.setUnitPrice(getUnitPrice("new"));
        pro.setImportDate(new Date());
        
        if(isYes("Update Information?"))
        {
            SaveToFile(data, "Data\\lastbackup.bin");
            data.set((int)index, pro);
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
        SaveToFile(data, "Data\\lastbackup.bin");
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
    public void Restore()
    {
        boolean isOk=false;
        do
        {
            System.out.println("Restore : \n1.LastUpdate\n2.Back Up List\n0.Back");
            switch(getChoice(2))
            {
                case 1:
                    ArrayList<Products>data=ReadFromFile("Data\\lastbackup.bin");
                    if(data.isEmpty())
                    {
                        System.out.println("No data to back up!!!");
                        return;
                    }
                    if(isYes("Are you sure back up last Record?")){
                        SaveToFile(ReadFromFile("Data\\Products.bin"), "Data\\lastbackup.bin");              
                        SaveToFile(data, "Data\\Products.bin");
                        System.out.println("Restore Completed!!!");
                        savePage(1);
                        getLastPage();
                        return;
                    }
                    break;
                case 2:
                    {
                    File file=new File("Data\\Backup");
                        if(file.list().length==0)
                        {
                            System.out.println("No Back up Found!!");break;
                        }
                        System.out.println("List of Back Up : ");
                            for(int i=1;i<=file.list().length;i++)
                            {
                                System.out.println(i+"."+file.list()[i-1]);
                            }
                        System.out.println("0.Back");
                        int choice=0;
                        choice=getChoice(file.list().length);
                        if(choice<=0)break;
                        if(isYes("Restore "+file.list()[choice-1]+" ?"))
                        {
                            SaveToFile(ReadFromFile("Data\\Products.bin"), "Data\\lastbackup.bin");
                            SaveToFile(new ArrayList<Products>(), "Data\\last.bin");
                            SaveToFile(ReadFromFile("Data\\Backup\\"+file.list()[choice-1]), "Data\\Products.bin");
                          
                            System.out.print("Restore Complete!!\nPress Enter to Continue! : ");
                            new Scanner(System.in).nextLine();
                            savePage(1);
                            getLastPage();
                            return;
                        }
                    }
                    break;
                case 0:isOk=true;
                    break;
            }
        }while(!isOk);
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
    public void BackUp()
    {
        Date date=new Date();
        
        SimpleDateFormat fmt=new SimpleDateFormat("ddMMyyyy");
        String tmp=fmt.format(date)+date.getTime();
        System.out.println("Back Up Data Name : "+tmp);
        if(isYes("Back up "+tmp+".bin ?"))
        {
            SaveToFile(ReadFromFile("Data\\Products.bin"), "Data\\Backup\\"+tmp+".bin");
            System.out.println("Back Up Complete!!");
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
        org.nocrala.tools.texttablefmt.Table tb=new org.nocrala.tools.texttablefmt.Table(5,BorderStyle.DESIGN_CURTAIN_WIDE);
        CellStyle cs=new CellStyle(CellStyle.HorizontalAlign.left, CellStyle.AbbreviationStyle.crop, CellStyle.NullStyle.nullText);
        tb.setColumnWidth(0, 15, 15);
        tb.setColumnWidth(1, 15, 15);
        tb.setColumnWidth(2, 10, 10);
        tb.setColumnWidth(3, 10, 10);
        tb.setColumnWidth(4, 20, 20);
        tb.addCell("ID",cs);
        tb.addCell("Product Name",cs);
        tb.addCell("Unit Price",cs);
        tb.addCell("Stock Qty",cs);
        tb.addCell("Import Date",cs);
        for(Products p:getListProduct(data, (currentpage-1)*row,row))
        {
            tb.addCell(p.getID()+"",cs);
            tb.addCell(p.getName(),cs);
            tb.addCell("$ "+p.getUnitPrice(),cs);
            tb.addCell(p.getStockQty()+"",cs);
            tb.addCell(p.getStringDate(),cs);
        }
        //------------------------------------------
        tb.addCell("");
        tb.addCell("");
        tb.addCell("");
        tb.addCell("");
        tb.addCell("");
        //------------------------------------------
        org.nocrala.tools.texttablefmt.Table tb1=new org.nocrala.tools.texttablefmt.Table(5,BorderStyle.DESIGN_CURTAIN_WIDE);
        
        tb1.setColumnWidth(0, 15, 15);
        tb1.setColumnWidth(1, 15, 15);
        tb1.setColumnWidth(2, 10, 10);
        tb1.setColumnWidth(3, 10, 10);
        tb1.setColumnWidth(4, 20, 20);
        tb1.addCell("Page "+currentpage+"/"+getLastPage(data,row),4);
        tb1.addCell((data.size())+" Records");
        System.out.println(tb.render());
        System.out.println(tb1.render());
        
    }
    public List<Products> getListProduct(ArrayList<Products>data,long from,long count)
    {
        List<Products>tmp=new ArrayList<Products>();
        try{
        tmp=data.subList((int)from,(int)(from+count));
        }
        catch(IndexOutOfBoundsException ex)
        {
        tmp=data.subList((int)from,data.size());
        }
        return tmp;
    }
    public void testTB()
    {
    org.nocrala.tools.texttablefmt.Table tb=new org.nocrala.tools.texttablefmt.Table(5);
        tb.addCell("ID");
        tb.addCell("Product Name");
        tb.addCell("Unit Price");
        tb.addCell("Stock Qty");
        tb.addCell("Import Date");
        System.out.println(tb.render());
    }
    
    public void delete()
    {
    ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
        {
            System.out.println("No data to detele!!!");return;
        }
        long id= getID();
        if(id==-1)return;
        
        Products pro=null;
        if((pro=getProduct(data, id))==null)
        {
            System.out.println("Not Found!!!");
            return;
        }
        System.out.println("-----------------------------------------------");
        System.out.println(pro.toString());
        System.out.println("-----------------------------------------------");
        if(isYes("Are you sure want to delete record?"))
        {
            SaveToFile(data, "Data\\lastbackup.bin");
            data.remove(pro);
            SaveToFile(data, "Data\\Products.bin");
            System.out.println("Delete Completed!!!");
        }
        else
        {
            System.out.println("Delete canceled!!!");
        }
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
        System.out.println("-----------------------------------------------");
        System.out.println(pro.toString());
        System.out.println("-----------------------------------------------");
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
        if(data.isEmpty())
        {
            System.out.println("Not Found!!!");
            return;
        }
        boolean isOk=false;
        int currentrow=1;
        do
        { 
            Display(data,5,currentrow);
            try
            {
                System.out.println(LoadStringFile(new FileReader("SearchMenu.txt")));
            }catch(IOException ex){}
            switch(new Scanner(System.in).nextLine().trim().toLowerCase())
            {
                case "f": currentrow=1;
                        break;
                case "l": currentrow=(int)getLastPage(data,5);break;
                case "n": currentrow++;
                            if(currentrow>=getLastPage(data,5))currentrow=(int)getLastPage(data,5);  
                    break;
                case "p": currentrow--;
                            if(currentrow<=0)currentrow=1;
                    break;
                case "g":
                        currentrow=getChoice(1, (int)getLastPage(data,5));
                        if(currentrow==-1)return;
                    break;
                case "e":isOk=true; break;
                default:System.out.println("Invalid choice!!!"); break;
            }
            
        }while(!isOk);
        
        
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
    public int getChoice(int high)
    {
        int result=0;
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input Choice [ 0 - "+high+" ] : ");
            Scanner read=new Scanner(System.in);
            tmp=read.nextLine().trim();
            try
            {
            result=Integer.parseInt(tmp);
            if(result<0)result=-result;
            if(result<=high)
            isOk=true;
            }catch(NumberFormatException ex)
            {
                if(tmp.equalsIgnoreCase("exit"))return -1;
            }
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
        return result;
    }
    public int getChoice(int low,int high)
    {
        int result=0;
        String tmp="";
        boolean isOk=false;
        do
        {
            System.out.print("Input Choice [ "+low+" - "+high+" ] : ");
            Scanner read=new Scanner(System.in);
            tmp=read.nextLine().trim();
            try
            {
            result=Integer.parseInt(tmp);
            if(result<0)result=-result;
            if(result<=high&&result>=low)
            isOk=true;
            }catch(NumberFormatException ex)
            {
                if(tmp.equalsIgnoreCase("exit"))return -1;
            }
            if(!isOk)System.out.println("Invalid!");
        }while(!isOk);
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
    public long getCurrentPage()
    {
        long tmp=0;
        try {
            DataInputStream read=new DataInputStream(new FileInputStream("Data\\page.bin"));
            tmp=read.readLong();
            read.close();
        } catch (FileNotFoundException ex) {
            try {
                DataOutputStream write=new DataOutputStream(new FileOutputStream("Data\\page.bin"));
                write.writeLong(1);
                write.close();
                return getCurrentPage();
            } catch (FileNotFoundException ex1) {
            
            } catch (IOException ex1) {
                
            }
            return getCurrentPage();
        } catch (IOException ex) {
        
        }
    return tmp;
    }
    public long getRow()
    {
        long tmp=0;
        try {
            DataInputStream read=new DataInputStream(new FileInputStream("Data\\row.bin"));
            tmp=read.readLong();
            read.close();
        } catch (FileNotFoundException ex) {
            try {
                DataOutputStream write=new DataOutputStream(new FileOutputStream("Data\\row.bin"));
                write.writeLong(5);
                write.close();
            } catch (FileNotFoundException ex1) {
            
            } catch (IOException ex1) {
                
            }
            return getCurrentPage();
        } catch (IOException ex) {
        
        }
    return tmp;
    }
    public void setRow()
    {
        long row=getChoice(1,10);
        if(row==-1)return;
        try {
            DataOutputStream write=new DataOutputStream(new FileOutputStream("Data\\row.bin"));
            write.writeLong(row);
            write.close();
        } catch (FileNotFoundException ex1) {

        } catch (IOException ex1) {

        }
        savePage(1);
        getLastPage();
    }
    public void next()
    {
        long tmp=getCurrentPage()+1;
        if(tmp>getLastPage())tmp=getLastPage();
        savePage(tmp);
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
                        {
                            System.out.println("No data to display!!!");
                            return;
                        }
        Display(data,(int) getRow(), getCurrentPage());
    }
    public void pre()
    {
        long tmp=getCurrentPage()-1;
        if(tmp<1)tmp=1;
        savePage(tmp);
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
                        {
                            System.out.println("No data to display!!!");
                            return;
                        }
        Display(data,(int) getRow(), getCurrentPage());
    }
    public void first()
    {
        savePage(1);
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
                        {
                            System.out.println("No data to display!!!");
                            return;
                        }
        Display(data,(int) getRow(), getCurrentPage());
    }
    public long getLastPage()
    {
        double f=ReadFromFile("Data\\Products.bin").size()/getRow();
        long tmp=0;
        if(f*getRow()<ReadFromFile("Data\\Products.bin").size())tmp=(long)f+1;
        else
            tmp=(long)f;
        return tmp;
    }
    
    public long getLastPage(ArrayList<Products>data)
    {
        float f=data.size()/getRow();
    long tmp=0;
        if(f*getRow()<data.size())tmp=((long)f)+1;
        else
            tmp=(long)f;
            if(tmp==0)tmp=1;
        return tmp;
    }
    public long getLastPage(ArrayList<Products>data,int row)
    {
        float f=data.size()/row;
    long tmp=0;
        if(f*row<data.size())tmp=((long)f)+1;
        else
            tmp=(long)f;
        return tmp;
    }
    public void last()
    {
        long tmp=getLastPage();
        savePage(tmp);
        ArrayList<Products>data=ReadFromFile("Data\\Products.bin");
        if(data.isEmpty())
                        {
                            System.out.println("No data to display!!!");
                            return;
                        }
        Display(data,(int) getRow(), getCurrentPage());
    }
    public void saveRow(long Row)
    {
        try {
                DataOutputStream write=new DataOutputStream(new FileOutputStream("Data\\row.bin"));
                write.writeLong(Row);
                write.close();
            } catch (FileNotFoundException ex1) {
            
            } catch (IOException ex1) {
                
            }
    }
    public void savePage(long Page)
    {
        try {
                DataOutputStream write=new DataOutputStream(new FileOutputStream("Data\\page.bin"));
                write.writeLong(Page);
                write.close();
            } catch (FileNotFoundException ex1) {
            
            } catch (IOException ex1) {
                
            }
    }
    
}
