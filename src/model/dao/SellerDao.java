package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	void insert(Seller d);

	void update(Seller d);

	void deleteById(Integer id);

	Seller findById(Integer d);

	List<Seller> findAll();
	List<Seller> findByDepartment(Department d);
}
