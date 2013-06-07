package com.example.vtsfileexplorer;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Explorer extends ListActivity {
 
 private List<String> item = null;
 private List<String> path = null;
 private String root;
 private TextView myPath;
 
 private String currentPath;
 Comparator<? super File> comparator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        myPath = (TextView)findViewById(R.id.path);
        
        comparator = filecomparatorByAlphabetically;
        root = Environment.getExternalStorageDirectory().getPath();
        getDir(root);
        
        Button btnAlphabetically = (Button)findViewById(R.id.button_alphabetically);
        btnAlphabetically.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View arg0) {
    comparator = filecomparatorByAlphabetically;
    getDir(currentPath);
    
   }});
        
        Button btnLastDateModified = (Button)findViewById(R.id.button_lastDateModified);
        btnLastDateModified.setOnClickListener(new OnClickListener(){

   @Override
   public void onClick(View arg0) {
    comparator = filecomparatorByLastModified;
          getDir(currentPath);
    
   }});
        Button btnSize = (Button)findViewById(R.id.button_size);
        btnSize.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				comparator = filecomparatorBySize;
				getDir(currentPath);
			}
		});
        Button btnFormat = (Button)findViewById(R.id.button_format);
        btnFormat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				comparator = filecomparatorByFormat;
				getDir(currentPath);
			}
		});
    }
    
    private void getDir(String dirPath)
    {
     currentPath = dirPath;
     
     myPath.setText("Location: " + dirPath);
     item = new ArrayList<String>();
     path = new ArrayList<String>();
     File f = new File(dirPath);
     File[] files = f.listFiles();
     
     if(!dirPath.equals(root))
     {
      item.add(root);
      path.add(root);
      item.add("../");
      path.add(f.getParent()); 
     }
     
     Arrays.sort(files, comparator);
     
     for(int i=0; i < files.length; i++)
     {
      File file = files[i];
      //long size;
            
      if(!file.isHidden() && file.canRead()){
       path.add(file.getPath());
          if(file.isDirectory()){
        	  item.add(file.getName() + "/  folder  " + file.length()/1024);
          }else{
        	  String filearray[] = file.getName().split("\\.");
        	  String etn = filearray[filearray.length-1];
        	  item.add(file.getName() + "  " + etn + "  " + file.length()/1024);
          }
      } 
     }

     ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
     setListAdapter(fileList); 
    }
    
    Comparator<? super File> filecomparatorByLastModified = new Comparator<File>()
{  
	  public int compare(File file1, File file2) 
	  {	
	   if(file1.isDirectory())
	   {
	    if (file2.isDirectory())
	    {
	     return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
	    }
	    else
	    {
	     return -1;
	    }
	   }
	   else 
	   {
	    if (file2.isDirectory())
	    {
	     return 1;
	    }
	    else
	    {
	     return Long.valueOf(file1.lastModified()).compareTo(file2.lastModified());
	    }
	   }
	    
	  }  
 };

 Comparator<? super File> filecomparatorByFormat = new Comparator<File>()
{
	  
	 public int compare(File file1, File file2) 
	 {
		 
		if(file1.isDirectory())
		{
		 if (file2.isDirectory())
		 {
			 return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
		 }
		 else
		 {
			 return -1;
		 }
		}
		else 
		{
		 if (file2.isDirectory())
		 {
			 return 1;
		 }
		 else
		 {
			 String file1Array[] = file1.getName().split("\\.");
			 String file2Array[] = file2.getName().split("\\.");
			 String etn1 = file1Array[file1Array.length-1];
			 String etn2 = file2Array[file2Array.length-1];
			 return String.valueOf(etn1.toLowerCase()).compareTo(etn2.toLowerCase());
		 }
		}
 
	 }  
};

    Comparator<? super File> filecomparatorByAlphabetically = new Comparator<File>()
{
  
	  public int compare(File file1, File file2) 
	  {
	
		   if(file1.isDirectory())
		   {
		    if (file2.isDirectory())
		    {
		    	return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
		    }
		    else
		    {
		    	return -1;
		    }
		   }
		   else 
		   {
		    if (file2.isDirectory())
		    {
		    	return 1;
		    }
		    else
		    {
		    	return String.valueOf(file1.getName().toLowerCase()).compareTo(file2.getName().toLowerCase());
		    }
		   }
		    
	  }  
 };
 
 Comparator<? super File> filecomparatorBySize = new Comparator<File>()
{	  
		public int compare(File file1, File file2) 
		{	
			if(file1.isDirectory())
			{
			 if (file2.isDirectory())
			 {
				 return Long.valueOf(file1.length()).compareTo(file2.length());
			 }
			 else
			 {
				 return -1;
			 }
			}
			else 
			{
			 if (file2.isDirectory())
			 {
				 return 1;
			 }
			 else
			 {
				 return Long.valueOf(file1.length()).compareTo(file2.length());
			 }
			}
		 
		}  
};

 
 @Override
 protected void onListItemClick(ListView l, View v, int position, long id) {
  // TODO Auto-generated method stub
  File file = new File(path.get(position));
  
  if (file.isDirectory())
  {
   if(file.canRead()){
    getDir(path.get(position));
   }else{
    new AlertDialog.Builder(this)
     .setIcon(R.drawable.ic_launcher)
     .setTitle(file.getName() + " folder can't be read!")
     .setPositiveButton("OK", null).show(); 
   } 
  }else {
   new AlertDialog.Builder(this)	
     .setIcon(R.drawable.ic_launcher)
     .setTitle(file.getName() + "  " + file.length())
     .setPositiveButton("OK", null).show();

    }
 }

}