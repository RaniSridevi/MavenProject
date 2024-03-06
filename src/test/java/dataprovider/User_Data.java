package dataprovider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class User_Data {
	
	public Object[][]readfile() throws EncryptedDocumentException, IOException{
		FileInputStream file=new FileInputStream("D:\\Eclipse\\workspace\\Demo\\src\\main\\resources\\Project_Data.xlsx");
		
		Workbook book=WorkbookFactory.create(file);
		
		Sheet sheet=book.getSheet("TestData");
		
		int row=sheet.getLastRowNum();
		int column=sheet.getRow(4).getLastCellNum();
		
		Object [][]obj=new Object[row][column];
		
		for(int i=0; i<row; i++) {
			
			for(int j=0; j<column; j++) {
				obj[i][j]=sheet.getRow(i+1).getCell(j).getStringCellValue();
				
			}
		}
	
		return obj;
	}
	
	

}

