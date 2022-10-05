package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controller.StudentController;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.EnrollmentRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.service.GradebookService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { StudentController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestStudent {
	static final String URL = "http://localhost:8080";
	public static final int TEST_STUDENT_ID = 40442;
	public static final String TEST_STUDENT_EMAIL = "test123456@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test123456";
	
	public static final String TEST_STUDENT_STATUS_NO_HOLD = null;
	public static final int TEST_STUDENT_STATUS_CODE_NO_HOLD = 0;

	public static final String TEST_STUDENT_STATUS_WITH_HOLD = "HOLD";
	public static final int TEST_STUDENT_STATUS_CODE_WITH_HOLD = 1;
	
	@MockBean
	CourseRepository courseRepository;

	@MockBean
	StudentRepository studentRepository;

	@MockBean
	EnrollmentRepository enrollmentRepository;

	@MockBean
	GradebookService gradebookService;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void addStudentSuccessful() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = TEST_STUDENT_NAME;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_NO_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_NO_HOLD);

		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);

		response = mvc.perform(MockMvcRequestBuilders
			.post("/student")
			.characterEncoding("utf-8")
			.content(asJsonString(studentDTO))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();
		System.out.println(response.getContentAsString());
		
		// check for 200 OK
		assertEquals(200, response.getStatus());
		// check values of returned student
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertEquals(TEST_STUDENT_EMAIL, result.email);
		assertEquals(TEST_STUDENT_NAME, result.name);

		verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void addStudentBadRequestNoName() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = null;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_NO_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_NO_HOLD);

		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);

		response = mvc.perform(MockMvcRequestBuilders
			.post("/student")
			.characterEncoding("utf-8")
			.content(asJsonString(studentDTO))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();
		System.out.println(response.getContentAsString());
		
		// check for 400 bad request
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void addStudentBadRequestNoEmail() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = null;
		studentDTO.name = TEST_STUDENT_NAME;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_NO_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_NO_HOLD);

		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);

		response = mvc.perform(MockMvcRequestBuilders
			.post("/student")
			.characterEncoding("utf-8")
			.content(asJsonString(studentDTO))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();
		System.out.println(response.getContentAsString());
		
		// check for 400 bad request
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void addStudentBadRequestNoEmailNoName() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = null;
		studentDTO.name = null;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_NO_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_NO_HOLD);

		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(null);
		given(studentRepository.save(any(Student.class))).willReturn(student);

		response = mvc.perform(MockMvcRequestBuilders
			.post("/student")
			.characterEncoding("utf-8")
			.content(asJsonString(studentDTO))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andReturn().getResponse();
		System.out.println(response.getContentAsString());
		
		// check for 400 bad request
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void placeHoldSuccessful() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = TEST_STUDENT_NAME;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_NO_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_NO_HOLD);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		response = mvc.perform(MockMvcRequestBuilders
				.put("/student/hold/place")
				.characterEncoding("utf-8")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		System.out.println(response.getContentAsString());

		// check for 200 OK
		assertEquals(200, response.getStatus());

		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertEquals(result.statusCode, TEST_STUDENT_STATUS_CODE_WITH_HOLD);
		assertEquals(result.status, TEST_STUDENT_STATUS_WITH_HOLD);
	}
	
	@Test
	public void removeHoldSuccessful() throws Exception {
		MockHttpServletResponse response;
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.email = TEST_STUDENT_EMAIL;
		studentDTO.name = TEST_STUDENT_NAME;

		Student student = new Student();
		student.setName(TEST_STUDENT_NAME);
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setStatusCode(TEST_STUDENT_STATUS_CODE_WITH_HOLD);
		student.setStatus(TEST_STUDENT_STATUS_WITH_HOLD);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		
		response = mvc.perform(MockMvcRequestBuilders
				.put("/student/hold/remove")
				.characterEncoding("utf-8")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andReturn().getResponse();
		System.out.println(response.getContentAsString());

		// check for 200 OK
		assertEquals(200, response.getStatus());
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertEquals(result.statusCode, TEST_STUDENT_STATUS_CODE_NO_HOLD);
		assertEquals(result.status, TEST_STUDENT_STATUS_NO_HOLD);
	}
	
	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
