/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cmd;
import code.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
/**
 *
 * @author Leanghour
 */
public class test {
    
    public static void main(String[] args) {
        ArrayList<Products>data=new ArrayList<Products>();
        for(long i=0;i<10000000;i++)
        {
         data.add(new Products(getLastID(data)));
        }
        SaveToFile(data, "test.bin");
    }
    public static long getLastID(ArrayList<Products>data)
    {
        return (data.size()>0)?data.get(data.size()-1).getID()+1:1;
    }
    public static void SaveToFile(ArrayList<Products>data,String FilePath)
    {
        try {
            long start=System.currentTimeMillis();
            ObjectOutputStream write=new ObjectOutputStream(new FileOutputStream(FilePath));
            write.writeObject(data);
            write.close();
            long stop=System.currentTimeMillis();
            System.out.print("time to write = "+((stop-start)/1000));
        } catch (IOException ex) {
        
        }
    }
    
}
