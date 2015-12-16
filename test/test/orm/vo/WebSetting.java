package test.orm.vo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.joker23.orm.persistence.POJO;

@Table(name="websetting")
public class WebSetting extends POJO{

	private static final long serialVersionUID = -6164683584481821L;

	@Id
	private Long id;
	
	private String webName;
	
	private String doamin;
	
	private String keyWord;
	
	private String description;
	
	private String icp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWebName() {
		return webName;
	}

	public void setWebName(String webName) {
		this.webName = webName;
	}

	public String getDoamin() {
		return doamin;
	}

	public void setDoamin(String doamin) {
		this.doamin = doamin;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIcp() {
		return icp;
	}

	public void setIcp(String icp) {
		this.icp = icp;
	}
	
}
