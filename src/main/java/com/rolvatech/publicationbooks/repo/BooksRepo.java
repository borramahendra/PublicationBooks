package com.rolvatech.publicationbooks.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rolvatech.publicationbooks.model.BooksModel;

@Repository
public interface BooksRepo extends JpaRepository<BooksModel, Integer> {

}
