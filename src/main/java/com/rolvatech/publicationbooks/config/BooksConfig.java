package com.rolvatech.publicationbooks.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.rolvatech.publicationbooks.model.BooksModel;
import com.rolvatech.publicationbooks.repo.BooksRepo;


@Configuration
@EnableBatchProcessing
public class BooksConfig {
	@Autowired
	BooksRepo booksRepo;
	
	@Autowired
	BooksItemProcessor booksItemProcessor;
	@Autowired
	BooksItemwriter booksItemwriter;
	
	@Bean
	public FlatFileItemReader<BooksModel> reader() {
	    FlatFileItemReader<BooksModel> reader = new FlatFileItemReader<>();
	    reader.setResource(new ClassPathResource("AutherBooks.csv"));
	    reader.setLinesToSkip(1);

	    DefaultLineMapper<BooksModel> mapper = new DefaultLineMapper<>();

	    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
	    tokenizer.setDelimiter(",");
	    tokenizer.setQuoteCharacter('"'); 
	    tokenizer.setStrict(false);
	    tokenizer.setNames("document_Title", "authors", "author_Affiliations", "publication_Title",
				"funding_Information");

	    BeanWrapperFieldSetMapper<BooksModel> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
	    fieldSetMapper.setTargetType(BooksModel.class);

	    mapper.setLineTokenizer(tokenizer);
	    mapper.setFieldSetMapper(fieldSetMapper);

	    reader.setLineMapper(mapper);

	    return reader;
	}
	
	
	@Bean
	public Step step(JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {
		return new  StepBuilder("step1",jobRepository)
				.<BooksModel,BooksModel>chunk(10,transactionManager)
				.reader(reader())
				.processor(booksItemProcessor)
				.writer(booksItemwriter)
				.build();	
	}
	
	@Bean
	public Job job(JobRepository jobRepository, @Qualifier("step") Step step1) {
		return new JobBuilder("job",jobRepository)
				.flow(step1)
				.end()
				.build();
		
	}

}
