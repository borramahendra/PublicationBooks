package com.rolvatech.publicationbooks.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
public class BooksController {
	
	@Autowired
	JobLauncher jobLauncher;
	@Autowired
	Job job;
	@Autowired
	@Qualifier("dbToCsvJob")
	Job dbToCsvJob;
	
	@PostMapping("/save")
	public ResponseEntity<String> runBatchProcess() {

	    JobParameters jobParameters = new JobParametersBuilder()
	            .addLong("startAt", System.currentTimeMillis())
	            .toJobParameters();

	    try {
	        jobLauncher.run(job, jobParameters);
	        return ResponseEntity.ok("CSV to DB Job Started");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Job Failed: " + e.getMessage());
	    }
	}
	
	
	@GetMapping("/export")
	public ResponseEntity<String> exportData() {

	    JobParameters jobParameters = new JobParametersBuilder()
	            .addLong("startAt", System.currentTimeMillis())
	            .toJobParameters();

	    try {
	        jobLauncher.run(dbToCsvJob, jobParameters);
	        return ResponseEntity.ok("CSV Export Started");

	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Export Failed: " + e.getMessage());
	    }
	}

}
