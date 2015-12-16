package test.orm;

import java.util.ArrayList;
import java.util.List;

import test.orm.vo.Carousel;
import test.orm.vo.Item;
import test.orm.vo.MergeTrade;
import test.orm.vo.PrintTradeHelper;
import test.orm.vo.WebSetting;

import com.joker23.orm.connection.ConnectionFactory;
import com.joker23.orm.dao.BaseDao;
import com.joker23.orm.dao.Dao;
import com.joker23.orm.util.GenerateSqlUtil;

public class CommTest {
	
	public static Dao<Item> dao = new BaseDao<Item>(Item.class, "test");
	
	public static void main(String[] args) {
		try{
			//ConnectionFactory.init("test", "druid.properties");
			
			String sql = GenerateSqlUtil.generateSQL(new Carousel());
			System.out.println(sql);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sqlTest() throws Exception{
//		String sql = "update item set cid=9 where id=12";
//		String sql = "select id,cid,title from item where cid = ? and title = ?";
		
//		List<Item> list = new ArrayList<Item>();
//		
//		list = dao.query(sql, 2, "bb");
//		
//		System.out.println("size: " + list.size());
//		for(Item item : list){
//			System.out.println("-----------------------");
//			System.out.println("id: " + item.getId());
//			System.out.println("cid: " + item.getCid());
//			System.out.println("title: " + item.getTitle());
//			System.out.println("time: " + item.getModifyTime());
//		}
		
		String sql = "select cid,count(id) from item group by cid";
		List<List<Object>> list = dao.executeQuery(sql);
		
		for(List<Object> row : list){
			System.out.println("----------------");
			
			for(Object obj : row){
				System.out.print(obj + "  ");
			}
			System.out.println();
			
		}
	}
	
	public static void countTest() throws Exception {
		Item item = new Item();
		item.setCid(2l);
		item.setId(12l);
		System.out.println(dao.count(null, "title='bb'"));
	}
	
	public static void filterTest() throws Exception {
		List<Item> list = new ArrayList<Item>();
		
		Item entity = new Item();
		entity.setCid(2l);
		
		String condition = "title = 'bb'";
		
		list = dao.filter(condition, 1, 10, "id desc,cid asc");
		
		
		System.out.println("size: " + list.size());
		for(Item item : list){
			System.out.println("-----------------------");
			System.out.println("id: " + item.getId());
			System.out.println("cid: " + item.getCid());
			System.out.println("title: " + item.getTitle());
			System.out.println("time: " + item.getModifyTime());
		}
	}
	
	public static void listTest() throws Exception{
		List<Item> list = new ArrayList<Item>();
		
//		List<Long> ids = new ArrayList<Long>();
//		ids.add(9L);
//		ids.add(10l);
//		list = dao.list(ids);
		
//		Item entity = new Item();
//		entity.setCid(2l);
//		list = dao.list(entity, 1, 1);
		
		list = dao.listAll();
		
		System.out.println("size: " + list.size());
		for(Item item : list){
			System.out.println("-----------------------");
			System.out.println("id: " + item.getId());
			System.out.println("cid: " + item.getCid());
			System.out.println("title: " + item.getTitle());
			System.out.println("time: " + item.getModifyTime());
		}
	}
	
	public static void getTest() throws Exception {
		Item item = new Item();
		
//		item = dao.get(9L, "id,cid");
		
//		item.setId(10l);
//		item = dao.get(item);
		
		
		
		System.out.println(item.getId());
		System.out.println(item.getCid());
		System.out.println(item.getTitle());
		System.out.println(item.getModifyTime());
		
	}
	
	public static void updateTest() throws Exception {
		List<Item> list = new ArrayList<Item>();
		for(int i = 12; i < 14; i++){
			Item item = new Item();
			item.setId((long)i);
			item.setCid(2l);
			item.setTitle("cc");
			
			List<String> sku = new ArrayList<String>();
			sku.add("adad");
			item.setSku(sku);
			
			list.add(item);
		}
		
		dao.batchUpdate(list);
	}
	
	public static void delTest() throws Exception {
		List<Long> list = new ArrayList<Long>();
		list.add(15L);
		list.add(16L);
		dao.batchDelete(list);
	}
	
	public static void saveTest() throws Exception{
		List<Item> list = new ArrayList<Item>();
		for(int i = 0; i < 3; i++){
			Item item = new Item();
			item.setCid((long)i);
			item.setTitle("balck");
			
			List<String> sku = new ArrayList<String>();
			sku.add("adad");
			item.setSku(sku);
			
			list.add(item);
		}
		dao.ignoreBatchSave(list);
	}
	
}

