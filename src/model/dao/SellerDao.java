package model.dao;

import java.util.List;

import model.entities.Seller;

public interface SellerDao {
	void insert(Seller d);

	void update(Seller d);

	void deleteById(Integer id);

	Seller findById(Seller d);

	List<Seller> findAll();
}
