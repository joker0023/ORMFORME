package test.orm.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.joker23.orm.persistence.POJO;

@Table(name="item")
public class Item extends POJO{

	private static final long serialVersionUID = -5336240724746108665L;

	@Id
	private Long id;
	
	private String title;
	
	@Id
	private Long cid;
	
	private Date modifyTime;
	
	@Transient
	private List<String> sku;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public List<String> getSku() {
		return sku;
	}

	public void setSku(List<String> sku) {
		this.sku = sku;
	}
}
