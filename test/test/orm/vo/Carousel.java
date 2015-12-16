package test.orm.vo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.joker23.orm.persistence.POJO;

@Table(name="carousel")
public class Carousel extends POJO{

	private static final long serialVersionUID = 6129369664725784828L;

	@Id
	private Long id;
	
	private String pic;
	
	private String url;
	
	private String title;
	
	private String content;
	
	private Integer sort;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
}
