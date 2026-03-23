package com.rolvatech.publicationbooks.model;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Document")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BooksModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name="Document Title",columnDefinition = "TEXT")
	private String document_Title;
	@Column(name="Authors",columnDefinition = "TEXT")
	private String authors;
	@Column(name="Author Affiliations", columnDefinition = "TEXT")
	private String author_Affiliations;
	@Column(name="Publication Title",columnDefinition = "TEXT")
	private String publication_Title;
	@Column(name="Funding Information",columnDefinition = "TEXT")
	private String funding_Information;

}
