package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {
	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller d) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT seller.*,department.Name as DepName " + " FROM seller INNER JOIN department "
							+ " ON seller.DepartmentId = department.ID " + " WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department d = instantiateDepartment(rs);
				Seller s = instantiateSeller(rs,d);
				return s;
				
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department d) throws SQLException {
		Seller s = new Seller();
		s.setId(rs.getInt("Id"));
		s.setName(rs.getString("Name"));
		s.setEmail(rs.getString("Email"));
		s.setBaseSalary(rs.getDouble("BaseSalary"));
		s.setBirthDate(rs.getDate("BirthDate"));
		s.setDepartment(d);
		return s;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department d= new Department();
		d.setId(rs.getInt("DepartmentId"));
		d.setName(rs.getString("DepName"));
		return d;
	}

	@Override
	public List<Seller> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
