package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@SpringBootTest
public class EndToEndStudentTest {
	public static final String CHROME_DRIVER_FILE_LOCATION = "/home/colby/chromedriver_linux64/chromedriver";
	public static final String URL = "http://localhost:3000";
	public static final String TEST_USER_EMAIL = "test@csumb.edu";
	public static final String TEST_USER_NAME = "Monte Otter"; 
	public static final int SLEEP_DURATION = 1000; // 1 second.

	@Autowired
	StudentRepository studentRepository;
	
	@Test
	public void addStudentTest() throws Exception {
		// Deleting the student with test email if it currently exists
		Student s = null;
		do {
			s = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (s != null) {
				studentRepository.delete(s);
			}
		} while (s != null);
		
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		// Wait 10 seconds before throwing an exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		try {
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			// Locate and click the Add Student button
			driver.findElement(By.xpath("//*[@id=\"root\"]/div/div/div/a[2]")).click();
			Thread.sleep(SLEEP_DURATION);
			// Fill in the test data in the name and email fields
			driver.findElement(By.xpath("//*[@id=\"nameField\"]")).sendKeys(TEST_USER_NAME);
			driver.findElement(By.xpath("//*[@id=\"emailField\"]")).sendKeys(TEST_USER_EMAIL);
			Thread.sleep(SLEEP_DURATION);
			// Locate and click the Add button
			driver.findElement(By.xpath("//*[@id=\"Add\"]")).click();
			Thread.sleep(SLEEP_DURATION);
			// Check the message in the toast popup
			String toastMessage = driver.findElement(By.className("Toastify__toast-body")).getText();
			assertEquals(toastMessage, "Student successfully added");
			// Verify that the student exists in the student repository
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			assertNotNull(student, "Student was added but not found in the database");
			assertEquals(student.getEmail(), TEST_USER_EMAIL);
			assertEquals(student.getName(), TEST_USER_NAME);
			
		} catch (Exception exception) {
			throw exception;
		} finally {
			// clean up database
			Student student = studentRepository.findByEmail(TEST_USER_EMAIL);
			if (student != null) {
				studentRepository.delete(student);
			}
			driver.quit();
		}
	}
}
