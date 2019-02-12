package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            //Creating JSON objects and arrays
            JSONObject jsonObject = new JSONObject();
            JSONArray rowHeader = new JSONArray(); //To contain the rowheader
            JSONArray colHeader = new JSONArray(); //Contains the column header
            JSONArray data = new JSONArray(); //Will store the numbers
            
            for(String string : iterator.next()){ 
                colHeader.add(string);
            }
            
            while(iterator.hasNext()){ //While the reader goes through the CSV file
               JSONArray grades = new JSONArray(); //To store the grades or numbers
               String[] rowData = iterator.next(); //This will grab the grades or numbers
               rowHeader.add(rowData[0]); //Will add the top row to the row header
               
               for(int i = 0; i < rowData.length;i++){
                   grades.add(Integer.parseInt(rowData[i])); //This will collect and contains the rest of the rows
               }
               
               data.add(grades); //The grades will be added to the data array
            }
            //Adding the converted data from the CSV to a JSON file
            jsonObject.put("ColumnHeaders",colHeader);
            jsonObject.put("RowHeaders",rowHeader);
            jsonObject.put("Data",data);
            
            //Making the converted data return as a string
            results = (jsonObject.toJSONString());
            
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            //Initalizing and acquring data from CVStoJson
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = new JSONObject();
            JSONArray rowHeader = (JSONArray)jsonObject.get("RowHeaders");
            JSONArray colHeader = (JSONArray)jsonObject.get("ColumnHeaders");
            JSONArray data = (JSONArray)jsonObject.get("Data");
            
            String[] rowStringArray = new String[rowHeader.size()];
            String[] colStringArray = new String[colHeader.size()];
            String[] dataStringArray = new String[data.size()];
            
            
            //Acquring the column string and putting it into a string aray
            for(int j = 0; j <colHeader.size(); j++){
                colStringArray[j] = colHeader.get(j).toString();
            }
            
            csvWriter.writeNext(colStringArray); //Writing the array into CSV format
            
            //Acquring the strings from the rows and putting them into an array
            for(int i = 0; i < rowHeader.size(); i++){
                rowStringArray[i] = rowHeader.get(i).toString();
                dataStringArray[i] = data.get(i).toString();
            }
            
            //Will sort through the rows(header and following) and convert to CSV
            for(int k = 0; k < dataStringArray.length; k++ ){
                JSONArray arrayData = (JSONArray)parser.parse(dataStringArray[k]);
                String[] rows = new String[arrayData.size() + 1];
                rows[0] = rowStringArray[k];
                
                for(int l = 0; l < arrayData.size(); l++){
                    rows[l+1] = arrayData.get(l).toString();
                }
                
                csvWriter.writeNext(rows);
            }
            
            results = writer.toString();
        }
        
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}