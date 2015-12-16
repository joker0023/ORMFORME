package test.orm.vo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.joker23.orm.persistence.POJO;

@Table(name="trade_mergetrade")
public class MergeTrade extends POJO{

	private static final long serialVersionUID = -7674625212017658077L;
	
	@Id
	private Long id;
	
	private Long sellerId;
	
	private String tids;
	
	private String sellerMemos;
	
	private String buyerMsgs;
	
	private String buyerNick;
	
	private String type;
	
	private String status;
	
	private Boolean printStatus;
	
	private String logisticsInfo;
	
	private Boolean hasMemoMsg;
	
	private Boolean isMerge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getTids() {
		return tids;
	}

	public void setTids(String tids) {
		this.tids = tids;
	}

	public String getSellerMemos() {
		return sellerMemos;
	}

	public void setSellerMemos(String sellerMemos) {
		this.sellerMemos = sellerMemos;
	}

	public String getBuyerMsgs() {
		return buyerMsgs;
	}

	public void setBuyerMsgs(String buyerMsgs) {
		this.buyerMsgs = buyerMsgs;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(Boolean printStatus) {
		this.printStatus = printStatus;
	}

	public String getLogisticsInfo() {
		return logisticsInfo;
	}

	public void setLogisticsInfo(String logisticsInfo) {
		this.logisticsInfo = logisticsInfo;
	}

	public Boolean getHasMemoMsg() {
		return hasMemoMsg;
	}

	public void setHasMemoMsg(Boolean hasMemoMsg) {
		this.hasMemoMsg = hasMemoMsg;
	}

	public Boolean getIsMerge() {
		return isMerge;
	}

	public void setIsMerge(Boolean isMerge) {
		this.isMerge = isMerge;
	}
}
