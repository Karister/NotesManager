package pl.arczynskiadam.core.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTES")
public class NoteVO {
	
	@Id
	@Column(name="ID")
	@GeneratedValue
	private Integer id;
	
	@Column(name="AUTHOR")
	private String author;
	
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="CONTENT")
	private String content;
	
	@Column(name="DATE_CREATED")
	private Date dateCreated;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String author) {
		this.email = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public String toString() {
		return "NoteVO [id=" + id + ", author=" + author + ", email=" + email
				+ ", content=" + content + ", dateCreated=" + dateCreated + "]";
	}
}