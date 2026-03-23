package com.rolvatech.publicationbooks.config;


import javax.sql.DataSource;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rolvatech.publicationbooks.model.BooksModel;
import com.rolvatech.publicationbooks.repo.BooksRepo;

@Component
public class BooksItemwriter implements ItemWriter<BooksModel> {
	@Autowired
	BooksRepo booksRepo;
	
	
	private JdbcBatchItemWriter<BooksModel> writer;
	
	public BooksItemwriter(DataSource dataSource) {

        this.writer = new JdbcBatchItemWriter<BooksModel>();

        this.writer.setDataSource(dataSource);
        this.writer.setSql(
            "insert into Document(document_Title,authors,author_Affiliations,publication_Title,funding_Information) " +
            "values(:document_Title,:authors,:author_Affiliations,:publication_Title,:funding_Information)"
        );

        this.writer.setItemSqlParameterSourceProvider(
            new BeanPropertyItemSqlParameterSourceProvider<>()
        );

        this.writer.afterPropertiesSet();
    }

	@Override
	public void write(Chunk<? extends BooksModel> chunk) throws Exception {
		booksRepo.saveAll(chunk.getItems());
		
	}

}
