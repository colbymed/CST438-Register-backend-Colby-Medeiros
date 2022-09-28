package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;

@RestController
public class StudentController {
	@Autowired
	StudentRepository studentRepository;
	
	@PostMapping("/student")
	@Transactional
	public Student createNewStudent(@RequestBody Student student) {
		Student s = studentRepository.findByEmail(student.getEmail());
		if (s == null) {
			System.out.println("Student with email " + student.getEmail() + " does not currently exist");
			studentRepository.save(student);
			return student;
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + student.getEmail() + " already exists.");
		}
	}
	
	@PostMapping("/student/hold/place")
	@Transactional
	public Student placeHold(@RequestBody Student student) {
		Student s = studentRepository.findByEmail(student.getEmail());
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
			return s;
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + student.getEmail() + " does not exist.");
		}
	}
	
	@PostMapping("/student/hold/remove")
	@Transactional
	public Student removeHold(@RequestBody Student student) {
		Student s = studentRepository.findByEmail(student.getEmail());
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
			return s;
		}
		else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student with email " + student.getEmail() + " does not exist.");
		}
	}
}
