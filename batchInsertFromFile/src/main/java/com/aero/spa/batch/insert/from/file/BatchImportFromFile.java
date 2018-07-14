package com.aero.spa.batch.insert.from.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * A simple batch to insert data from a file to a database. For now the only
 * table aimed will be a simple User
 * 
 * @author florentm007
 * @since 08/2018
 *
 */
@SpringBootApplication
public class BatchImportFromFile {
	public static void main(String[] args) {
		SpringApplication.run(BatchImportFromFile.class, args);
	}
}
