package com.sts4.demos.sbrest1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SbRest1Application {

	public static void main(String[] args) {
		SpringApplication.run(SbRest1Application.class, args);
	}

}

//-----

//domain
class Student {
	private static Logger log = LoggerFactory.getLogger(Student.class);
	private static AtomicInteger studentReqCount = new AtomicInteger(1);

	private int studentId;
	private String fname;
	private String lname;
	private int age;
	private LocalDate dob;

	public Student() {
		log.info("#######  Student Created ####");
	}

	public Student(int studentId, String fname, String lname, int age, LocalDate dob) {
		super();
		this.studentId = studentId;
		this.fname = fname;
		this.lname = lname;
		this.age = age;
		this.dob = dob;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

}

//mock service
@Service
class InMemoryStudentService {
	private static Logger log = LoggerFactory.getLogger(InMemoryStudentService.class);
	private static AtomicInteger studentServiceCount = new AtomicInteger(1);
	private static List<Student> mockStudents;

	static {
		initMockStudents();
	}

	public InMemoryStudentService() {
		log.info("#######  Student Service Created ####");
	}

	private static void initMockStudents() {
		log.info("####### Initialising mock students ####");
		mockStudents = new ArrayList<>();

		for (int i = 0; i < 100; i++) {
			mockStudents.add(new Student(i, "fname" + i, "lname" + i, i, LocalDate.now().minusDays(i)));
		}

		log.info("Loaded all students in momory db of size {}", mockStudents.size());
		log.info("Students in meomory db {}", mockStudents);

	}

	public List<Student> findAllStudents() {
		log.info("Service findAllStudents ");
		return mockStudents;
	}

}

@RestController
@RequestMapping("/api/v1")
class StudentController {
	private static Logger log = LoggerFactory.getLogger(StudentController.class);
	private static AtomicInteger studentReqCount = new AtomicInteger(1);

	@Autowired
	private InMemoryStudentService mockStudentService;

	@GetMapping("/students")
	public List<Student> getAllStudents() {
		log.info("Got getAllStudents request {}", studentReqCount.getAndIncrement());
		return mockStudentService.findAllStudents();
	}
}

@RestController
class HelloController {
	private static Logger log = LoggerFactory.getLogger(HelloController.class);
	private static AtomicInteger reqCount = new AtomicInteger(1);

	@GetMapping("/hello")
	public String sayGreeting() {
		log.info("Got request {}", reqCount);
		String ServerResponse = new StringBuilder().append(" Server response ").append(LocalDateTime.now()).toString();
		log.info("Server response {}", ServerResponse);
		return ServerResponse;
	}
}