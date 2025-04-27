package project.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import project.scheduler.Services.MockDataService;


@SpringBootApplication
public class Application {

	


	/**
	 * The program's main entry point
	 * 
	 * @param args	Potential passed arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		MockDataService mockDataService = new MockDataService();
		mockDataService.init();	
		//Mock Data Service to add data on startup commented out but left present just in case
	}

}
