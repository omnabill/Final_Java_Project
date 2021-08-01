package com.spring.SpringHello;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Page;

/**
 *
 * @authors ADM
 */
@SpringBootApplication
@RestController
public class SpringHelloApplication {
    static DataExploration eda;
    
    public static void main(String[] args) {
        eda =new DataExploration();
        
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8000/index.html"));
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(SpringHelloApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        SpringApplication.run(SpringHelloApplication.class, args);  
    }
        
          
        

    @ResponseBody
    @GetMapping("/structure")
    public String structure() throws IOException, URISyntaxException {
        return eda.getStructure().write().toString("html");
    }
    
    @GetMapping("/summary")
    public String summary() throws IOException, URISyntaxException {
        return eda.getSummary().write().toString("html");
    }
    
    @GetMapping("/CompanyCountTable")
    public String CompanyCount(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {
        int r =Integer.parseInt(rows);
        return eda.getCompanyCountTable(r).sortDescendingOn("count").write().toString("html");
    }
    @GetMapping("/CompanyGetGraph")
    public String CompanyCountGraph(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {
        int r =Integer.parseInt(rows);
        return Page.pageBuilder(eda.getCompanyCountGraph(r), "plot_div_id").build().toString();
    }
    
    @GetMapping("/TitleCountTable")
    public String TitleCountTable(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {
        int r =Integer.parseInt(rows);
        return eda.getTitleCountTable(r).sortDescendingOn("count").write().toString("html");
    }
      @GetMapping("/TitleCountGraph")
    public String TitleCountGraph(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {  
        int r =Integer.parseInt(rows);
        return Page.pageBuilder(eda.getTitleCountGraph(r), "plot_div_id").build().toString();
    }
    
    @GetMapping("/LocationCountTable")
    public String LocationCountTable(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {
        int r =Integer.parseInt(rows);
        return eda.getLocationCountTable(r).sortDescendingOn("count").write().toString("html");
    }
    @GetMapping("/LocationCountGraph")
    public String LocationCountGraph(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {  
        int r =Integer.parseInt(rows);
        return Page.pageBuilder(eda.getLocationCountGraph(r), "plot_div_id").build().toString();
    }
    
    @GetMapping("/SkillsCountTable")
    public String SkillsCountTable(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException {   
        int r =Integer.parseInt(rows);
        return eda.getSkillsCountTable(r).sortDescendingOn("count").write().toString("html");
    }
    
    @GetMapping("/SkillsCountGraph")
    public String SkillsCountGraph(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException { 
        int r =Integer.parseInt(rows);
        return Page.pageBuilder(eda.getSkillsCountGraph(r), "plot_div_id").build().toString();       
    }
    
    @GetMapping("/YearsOfExprince")
    public String YearsOfExprince(@RequestParam(value = "rows", defaultValue = "10") String rows) throws IOException, URISyntaxException { 
        int r =Integer.parseInt(rows);
        return eda.getYearsExprinceFactroizedTable(r).write().toString("html");    
    } 
    
    @GetMapping("/ClustringWithKMeans")
    public String ClustringWithKMeans(@RequestParam(value = "numClusters", defaultValue = "5") String numClusters) throws IOException, URISyntaxException { 
        int c =Integer.parseInt(numClusters);
        return Page.pageBuilder(eda.RunKmeans(c), "plot_div_id").build().toString();
        
    }

    
}
