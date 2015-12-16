package test.orm.vo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.joker23.orm.persistence.POJO;

@Table(name="trade_printtradehelper")
public class PrintTradeHelper  extends POJO{

	private static final long serialVersionUID = 5075582533142216836L;

	@Id
	private Long tid;
	
	private Long sellerId;
	
	private Long mergeTradeId;
	
	private Boolean printStatus;

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getMergeTradeId() {
		return mergeTradeId;
	}

	public void setMergeTradeId(Long mergeTradeId) {
		this.mergeTradeId = mergeTradeId;
	}

	public Boolean getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(Boolean printStatus) {
		this.printStatus = printStatus;
	}
}
