package com.rolvatech.publicationbooks.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.rolvatech.publicationbooks.model.BooksModel;

@Component
public class BooksItemProcessor implements ItemProcessor<BooksModel,BooksModel> {

	@Override
	public BooksModel process(BooksModel item) throws Exception {
		return item;
	}

}
