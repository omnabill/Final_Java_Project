/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.SpringHello;


import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.csv.CSVFormat;
import smile.data.DataFrame;
import smile.io.Read;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

/**
 *
 * @authors ADM
 */
public class DAO {
   
    public Table GETdata() throws IOException, URISyntaxException{
      
        DataFrame Wuzzuf = Read.csv("src/main/resources/data/Wuzzuf_Jobs.csv", CSVFormat.DEFAULT.withDelimiter(',')
                .withHeader("Title","Company","Location","Type","Level","YearsExp","Country","Skills")
                .withSkipHeaderRecord(true));
        Wuzzuf.omitNullRows();     
        
        String[] names = Wuzzuf.names();
        Table WuzzufTable = Table.create();
        
        for (int i=0; i<names.length; i++) 
        { 
        StringColumn collumn = StringColumn.create(names[i], Wuzzuf.column(i).toStringArray());
        WuzzufTable.addColumns(collumn);
        }
        WuzzufTable = WuzzufTable.dropDuplicateRows();
        
        return WuzzufTable;
    }
    
    
   

   
}
    

