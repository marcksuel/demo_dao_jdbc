package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		Seller seller = sellerDao.findById(3);
		System.out.println("____TESTE 1: Seller FindById");
		System.out.println(seller);
		
		System.out.println("____TESTE 2: Seller  FindByDepartment");
		Department d =new Department(2,null);
		List<Seller>list = sellerDao.findByDepartment(d);
		list.forEach(System.out::println);
		

		System.out.println("____TESTE 3: FindAll");
		list = sellerDao.findAll();
		list.forEach(System.out::println);
		
		System.out.println("____TESTE 4: Insert");
		Seller s = new Seller(null,"Max","max@gmail", new Date(), 4000.0, d);
		sellerDao.insert(s);
		System.out.println("New Seller ID: " + s.getId());
		
		System.out.println("____TESTE 5: Insert");
		seller = sellerDao.findById(1);
		sellerDao.update(seller);
		System.out.println(" Insert Complete");
		
		System.out.println("____TESTE 6: Delete");
		sellerDao.deleteById(5);
		System.out.println(" Delete Complete");
		
	}
}
