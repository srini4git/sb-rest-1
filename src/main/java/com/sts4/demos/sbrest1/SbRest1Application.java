package com.sts4.demos.sbrest1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SbRest1Application {

	public static void main(String[] args) {
		SpringApplication.run(SbRest1Application.class, args);
	}

}

//-----
//global error handling

@ControllerAdvice
class SbRest1GlobalExceptionHandler{
	private static Logger log = LoggerFactory.getLogger(SbRest1GlobalExceptionHandler.class);

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGlobalExceptions(Exception ex) {
		log.error("In handleGlobalExceptions - Processing exception {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<String> handleStudentApiExceptions(StudentNotFoundException ex) {
		log.error("In handleStudentApiExceptions - Processing exception {}", ex.getMessage());
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
		
	}
}


class StudentNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public StudentNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	public StudentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	public StudentNotFoundException(String message) {
		super(message);
	}
	public StudentNotFoundException(Throwable cause) {
		super(cause);
	}
}
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
			mockStudents.add(new Student(i, "fname" + i, "lname" + i, i, LocalDate.now().minusDays(100+i)));
		}

		log.info("Loaded all students in momory db of size {}", mockStudents.size());
		log.info("Students in meomory db {}", mockStudents);

	}

	public List<Student> findAllStudents() {
		log.info("Service findAllStudents ");
		return mockStudents;
	}
	
	public Student findStudentById(int id){
		log.info("Service findStudentById ");
		return queryInDb(id);
	}

	private Student queryInDb(int id) {
		log.info("Service queryInDb {} ", id);
		return mockStudents.stream().filter(s -> s.getStudentId() == id).findAny().orElse(null);
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
	
	@GetMapping("/students/{id}")
	public Student getStudentById(@PathVariable("id") int id) throws Exception {
		log.info("Got getStudentById request {} for id {}", studentReqCount.getAndIncrement(), id);
		Student foundStudent = mockStudentService.findStudentById(id);
		if(foundStudent != null) {
			log.info("Student is present ", foundStudent);
			return foundStudent;
		}else {
			log.error("Student with id {} not found ", id);
			//throw new Exception("404 Student NOT FOUND with id " + id);
			throw new StudentNotFoundException("404 Student NOT FOUND with id " + id);
		}
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