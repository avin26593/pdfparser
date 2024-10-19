package com.pdf.parser.nsdl;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import technology.tabula.*;
import technology.tabula.extractors.BasicExtractionAlgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class NsdlApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(NsdlApplication.class, args);

		// Load the PDF file
		File pdfFile = new File("C:\\Users\\DELL\\Downloads\\nsdl\\nsdl\\src\\main\\resources\\NSDL_repoert_AAUPP0915B.PDF");
		String password = "AAUPP0915B";
		PDDocument pdf = Loader.loadPDF(pdfFile, password);

		// Parse the PDF document
		ObjectExtractor extractor = new ObjectExtractor(pdf);
		PageIterator pages = extractor.extract();

		File outputFile = new File("extracted_table.txt");

		// Open a writer to write the table data to a .txt file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
		// Loop through each page and extract tables
		while (pages.hasNext()) {
			Page page = pages.next();

			// Use the BasicExtractionAlgorithm to extract tables from the page
			BasicExtractionAlgorithm extractorAlgorithm = new BasicExtractionAlgorithm();
			List<Table> tables = extractorAlgorithm.extract(page);

			// Print extracted tables
			for (Table table : tables) {
				List<List<RectangularTextContainer>> rows = table.getRows();
				for (List<RectangularTextContainer> row : rows) {
					for (RectangularTextContainer cell : row) {
						writer.write(cell.getText() + "\t"); // Print table cells
					}
					writer.write("\n"); // New line after each row
				}
				writer.write("---- End of Table ----");
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
