package com.rolvatech.publicationbooks.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.rolvatech.publicationbooks.model.BooksModel;
import com.rolvatech.publicationbooks.repo.BooksRepo;

@Configuration
@EnableBatchProcessing
public class BooksDBtoCsvConfig {

	@Autowired
	BooksRepo booksRepo;
	@Autowired
	DataSource dataSource;

	@Bean
	public JdbcCursorItemReader<BooksModel> dbToReader(DataSource dataSource) {

		JdbcCursorItemReader<BooksModel> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setSql("select * from Document");

		reader.setRowMapper((rs, rownum) -> {
			BooksModel bm = new BooksModel();
			bm.setId(rs.getInt("id"));
			bm.setDocument_Title(rs.getString("document title"));
			bm.setAuthors(rs.getString("authors"));
			bm.setAuthor_Affiliations(rs.getString("author affiliations"));
			bm.setPublication_Title(rs.getString("publication title"));
			bm.setFunding_Information(rs.getString("funding information"));

			return bm;

		});
		return reader;

	}

	@Bean
	public FlatFileItemWriter<BooksModel> dbToWriter() {

		FlatFileItemWriter<BooksModel> writer = new FlatFileItemWriter<>();

		writer.setResource(new FileSystemResource("C://Users/mahendra/Documents/1st problem/output.csv"));
		writer.setHeaderCallback(
				w -> w.write("id,document_Title,authors,author_Affiliations,publication_Title,funding_Information"));

		BeanWrapperFieldExtractor<BooksModel> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] { "id", "document_Title", "authors", "author_Affiliations",
				"publication_Title", "funding_Information" });

		DelimitedLineAggregator<BooksModel> aggregator = new DelimitedLineAggregator<>();
		aggregator.setDelimiter(",");
		aggregator.setQuoteCharacter("\"");
		aggregator.setFieldExtractor(fieldExtractor);

		writer.setLineAggregator(aggregator);

		return writer;
	}

	@Bean
	public Step exportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step2", jobRepository)
				.<BooksModel, BooksModel>chunk(10, transactionManager)
				.reader(dbToReader(dataSource))
				.writer(dbToWriter())
				.build();
	}

	@Bean
	public Job dbToCsvJob(JobRepository jobRepository, @Qualifier("exportStep") Step step2) {
		return new JobBuilder("dbToCsvJob", jobRepository)
				.flow(step2)
				.end()
				.build();
	}
}
