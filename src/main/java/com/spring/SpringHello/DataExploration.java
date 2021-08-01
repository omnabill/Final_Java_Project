/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.SpringHello;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import smile.clustering.XMeans;
import smile.data.DataFrame;
import smile.data.measure.NominalScale;
import smile.data.vector.IntVector;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.api.*;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.components.Margin;

/**
 *
 * @authors Omar Nabil , Abdelrahman Elghamry,Amr Ghitany
 */
public class DataExploration {
    Table data;
    public DataExploration(){
        try {
            data = new DAO().GETdata();
            
        } catch (IOException ex) {
            Logger.getLogger(DataExploration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(DataExploration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    public Table getStructure(){ 
        return data.structure();
    }
    public Table getSummary(){
    
        return data.summary();
    }
  
    public Table getCompanyCountTable(int rows){

        return data.countBy("Company").sortAscendingOn("Count" ).last(rows);
    }
    public Figure getCompanyCountGraph(int rows){
        Figure fig = PiePlot.create("Jobs for each Company", getCompanyCountTable( rows), "Category", "Count");
        return figerBuilder(fig,1000,600,150);
        
    }
    
    public Table getTitleCountTable(int rows){
        return data.countBy("Title").sortAscendingOn("Count" ).last(rows);
    }
    
    public Figure getTitleCountGraph(int rows){
        Figure fig = HorizontalBarPlot.create("Most popular job Titles",
                getTitleCountTable(rows), "Category", "Count");
        return figerBuilder(fig,800,600,250);
        
    }
    
    public Table getCountryCountTable(int rows){
        return data.countBy("Country").sortAscendingOn("Count" ).last(rows);
    }
    public Figure getCountryCountGraph(int rows){
        Figure fig = HorizontalBarPlot.create("Most popular job by Country", getCountryCountTable(rows), "Category",
                "Count");
        return figerBuilder(fig,1000,500,300);
    }
    
    public Table getLocationCountTable(int rows){
        return data.countBy("Location").sortAscendingOn("Count" ).last(rows);
    }
    
    public Figure getLocationCountGraph(int rows){
        Figure fig = HorizontalBarPlot.create("Most popular jobs by Location", getLocationCountTable(rows),
                "Category", "Count");
        return figerBuilder(fig,800,600,250);
    }
    
    public Table getSkillsCountTable(int rows){
        StringColumn st = StringColumn.create("skills");   
        for (int i=0; i<data.rowCount(); i++) 
        { 
           st =  st.addAll(Arrays.asList(data.get(i, 7).toString().split(",")));
        }
        return Table.create(st).countBy("skills").sortAscendingOn("Count" ).last(rows);
    }     
    public Figure getSkillsCountGraph(int rows){  
        Figure fig = HorizontalBarPlot.create("demandeed", getSkillsCountTable( rows), "Category", "Count");
        return figerBuilder(fig,800,600,250);
    } 
    
     public Table getYearsExprinceFactroizedTable(int rows){
        StringColumn YearsExpFac = StringColumn.create("Exp"); 
        for (int i=0; i<data.rowCount(); i++) 
        { 
            YearsExpFac =  YearsExpFac.addAll(Arrays.asList(data.get(i, 5).toString().split(" ")[0])); 
        }        
        DataFrame df = Table.create(YearsExpFac).smile().toDataFrame();
        df = df.merge(IntVector.of("ExpFac", Encoder(df, "Exp")));
        String[] names = df .names();
        Table table = Table.create();
        
        for (int i=0; i<names.length; i++) 
        { 
        StringColumn collumn = StringColumn.create(names[i], df.column(i).toStringArray());
        table.addColumns(collumn);
        }
        
        return table.first(rows);
    } 
       
   public static int[] Encoder(DataFrame df, String columnName) {
        String[] values = df.stringVector(columnName).distinct().toArray(new String[] {});
        int[] pclassValues = df.stringVector(columnName).factorize(new NominalScale(values)).toIntArray();
        return pclassValues;
    }
   
    public Figure RunKmeans(int nclusters){
        DataFrame df = data.select("Company","Title").smile().toDataFrame();
        DataFrame dfTrain = DataFrame.of(IntVector.of("Company", Encoder(df, "Company")));
        dfTrain = dfTrain.merge(IntVector.of("Title", Encoder(df, "Title")));
        
        var clusters = XMeans.fit(dfTrain.toArray()  , nclusters);
        dfTrain = dfTrain.merge(IntVector.of("cat",clusters.y ));
        
        String[] names = dfTrain.names();
        Table tablek = Table.create();
        
        for (int i=0; i<names.length; i++) 
        { 
        IntColumn collumn = IntColumn.create(names[i], dfTrain.column(i).toIntArray());
        tablek.addColumns(collumn);
        }
        Figure fig = ScatterPlot.create("fig",tablek, "Title", "Company", "cat");
        return figerBuilder(fig,800,600,250);        
    } 
    
    private Figure figerBuilder(Figure fig,int width ,int hight ,int leftmargen ){
        fig.setLayout(Layout.builder().margin(Margin.builder().left(leftmargen).build())
                        .autosize(true).width(width).height(hight)
                        .paperBgColor("red")
                        .build());
        return fig;   
    }
 
}
