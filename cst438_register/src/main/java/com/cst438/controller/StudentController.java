package com.cst438.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://registerf-cst438.herokuapp.com/"})
public class StudentController {
	@Autowired
	StudentRepository studentRepository;
	
	@GetMapping("/student/{id}")
	public StudentDTO getStudent(@PathVariable Integer id) {
		if (id == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find a student with no id.");
		}
		Optional<Student> s = studentRepository.findById(id);
		if (!s.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No student found with id " + id);
		}
		return createStudentDTO(s.get());
	}
	
	@PostMapping("/student")
	@Transactional
	public StudentDTO createNewStudent(@RequestBody StudentDTO studentDTO) {
		if (studentDTO.email == null && studentDTO.name == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create a student without an email and name.");
		}
		else if (studentDTO.email == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create a student without an email.");
		}
		else if (studentDTO.name == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot create a student without a name.");
		}
		Student s = studentRepository.findByEmail(studentDTO.email);
		if (s == null) {
			System.out.println("Student with email " + studentDTO.email + " does not currently exist");
			s = new Student();
			s.setEmail(studentDTO.email);
			s.setName(studentDTO.name);
			studentRepository.save(s);
			return createStudentDTO(s);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + studentDTO.email + " already exists.");
		}
	}
	
	@PutMapping("/student/hold/place")
	@Transactional
	public StudentDTO placeHold(@RequestBody StudentDTO student) {
		Student s = studentRepository.findByEmail(student.email);
		if (s != null) {
			if (s.getStatusCode() == 0) {
				s.setStatusCode(1);
				s.setStatus("HOLD");
				studentRepository.save(s);
				System.out.println("Hold placed on student with email " + s.getEmail());
			}
			else {
				System.out.println("Student with email " + s.getEmail() + " already has a hold.");
			}
			return createStudentDTO(s);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + student.email + " does not exist.");
		}
	}
	
	@PutMapping("/student/hold/remove")
	@Transactional
	public StudentDTO removeHold(@RequestBody StudentDTO student) {
		Student s = studentRepository.findByEmail(student.email);
		if (s != null) {
			if (s.getStatusCode() == 1) {
				s.setStatusCode(0);
				s.setStatus(null);
				studentRepository.save(s);
				System.out.println("Hold removed from student with email " + s.getEmail());
			}
			else {
				System.out.println("Student with email " + s.getEmail() + " does not have a hold.");
			}
			return createStudentDTO(s);
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + student.email + " does not exist.");
		}
	}
	
	public StudentDTO createStudentDTO(Student s) {
		StudentDTO dto = new StudentDTO();
		dto.student_id = s.getStudent_id();
		dto.email = s.getEmail();
		dto.name = s.getName();
		dto.status = s.getStatus();
		dto.statusCode = s.getStatusCode();
		return dto;
	}
}
