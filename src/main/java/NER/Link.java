package NER;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author n
 */
public class Link {
    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search";
    public static void main(String[] args) throws IOException
    {
    
        System.out.print(findLinkCases("BOCG","other","Abreviation"));
    }
    public static String FindLinkGeneral(String searchTerm, int num) throws IOException
    {
        		
		String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;

		org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
		org.jsoup.select.Elements results = doc.select("h3.r > a");
                String finalResult = "";
                for (org.jsoup.nodes.Element result : results) {
                    String linkHref = result.attr("href");
                    String linkText = result.text();
                    //System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
                    String s = linkHref.substring(6, linkHref.indexOf("&"));
                    //take first link
                    s= s.replace("=", "");
                    finalResult = s;
                    break;   
                }
                return finalResult;
    }
    public static String FindLinkBOE(String searchTerm, int num) throws IOException
    {
        		
		String searchURL = GOOGLE_SEARCH_URL + "?q="+searchTerm+"&num="+num;

		org.jsoup.nodes.Document doc = Jsoup.connect(searchURL).userAgent("Mozilla/5.0").get();
		org.jsoup.select.Elements results = doc.select("h3.r > a");
                String finalResult = "";
                for (org.jsoup.nodes.Element result : results) {
                    String linkHref = result.attr("href");
                    String linkText = result.text();
                    //System.out.println("Text::" + linkText + ", URL::" + linkHref.substring(6, linkHref.indexOf("&")));
                    String s = linkHref.substring(6, linkHref.indexOf("&"));
                    //take first link
                   if(s.startsWith("=https://www.boe.es/"))
                    {
                            s= s.replace("=", "");
                            s= s.replace("%3F","?");
                            s= s.replace("%3D", "=");
                            finalResult = s;
                            break;
                    }    
                }
                return finalResult;
    }
	public static String findLinkCases(String searchTerm, String Type, String Entity) throws IOException {
		int num = 20;
                String finalResult = "";
                
                if(Type == "rule" || Type == "nickname")
                {
                    if(Entity != "Person" || Entity != "Organization" || Entity != "Other")
                    {
                        
                        finalResult = FindLinkBOE(searchTerm, num);
                    }
                    else if(Entity == "Person" || Entity == "Organization")
                    {
                        finalResult = FindLinkGeneral(searchTerm, num);
                    }
                }
                else if (Type == "nickname")
                {
                    //do search in its own excel file
                    
                    finalResult = FindLinkBOE(foundExcelNICk(searchTerm),num);
                }
                else if(Type == "other")
                {
                    if(Entity == "Abreviation" )
                    {
                       //do lookup in excel file then search on google
                        finalResult = FindLinkGeneral(foundExcelABR(searchTerm), num);          
                    }
                    else if(Entity == "Person" || Entity == "Organization" || Entity == "Governemental_Institution" || Entity == "Legal_reference")
                    {
                        finalResult = FindLinkGeneral(searchTerm, num);
                    }
                }                                                		
            return finalResult;
	}
        
        public static String foundExcelNICk(String term) throws FileNotFoundException, IOException
        {
            // Define the template file
            String xlsTemplate = "resources/inputText/abreviation2.csv.xlsx";

            try {
                    // Read spreadsheet template file to input stream
                    FileInputStream fis = new FileInputStream(new File(xlsTemplate));
                    // Create workbook object using file input stream
                    XSSFWorkbook wbTemplate = new XSSFWorkbook(fis);
                    // Get the first sheet from the workbook object
                    XSSFSheet sheet1 = wbTemplate.getSheetAt(0);

                    // Repeat for Cells D3 to D7
                    for(int RowIdx=1; RowIdx < 346; RowIdx++) {
                            XSSFRow col1 = sheet1.getRow(RowIdx);
                            XSSFCell data1= col1.getCell(0);
                            XSSFRow col2 = sheet1.getRow(RowIdx);
                            XSSFCell data2= col2.getCell(1);
                            if(term.trim().contentEquals(data1.toString()))
                                return data2.toString();
                    }
                    fis.close();
            }
            //Catch file not found exception
            catch (FileNotFoundException e) {
            }
            //Catch all other IO exceptions
            catch (IOException e) {
                System.err.println("Caught IOException" + e.getMessage());
            }
            return "none";
        
    }
        public static String foundExcelABR(String term) throws FileNotFoundException, IOException
        {
            // Define the template file
            String xlsTemplate = "resources/inputText/abreviation2.csv.xlsx";

            try {
                    // Read spreadsheet template file to input stream
                    FileInputStream fis = new FileInputStream(new File(xlsTemplate));
                    // Create workbook object using file input stream
                    XSSFWorkbook wbTemplate = new XSSFWorkbook(fis);
                    // Get the first sheet from the workbook object
                    XSSFSheet sheet1 = wbTemplate.getSheetAt(0);

                    // Repeat for Cells D3 to D7
                    for(int RowIdx=1; RowIdx < 346; RowIdx++) {
                            XSSFRow col1 = sheet1.getRow(RowIdx);
                            XSSFCell data1= col1.getCell(0);
                            XSSFRow col2 = sheet1.getRow(RowIdx);
                            XSSFCell data2= col2.getCell(1);
                            if(term.trim().contentEquals(data1.toString()))
                                return data2.toString();
                    }
                    fis.close();
            }
            //Catch file not found exception
            catch (FileNotFoundException e) {
            }
            //Catch all other IO exceptions
            catch (IOException e) {
                System.err.println("Caught IOException" + e.getMessage());
            }
            return "none";
        
    }
}