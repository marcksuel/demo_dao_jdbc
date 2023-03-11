package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
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
	public void insert(Seller s) {
		PreparedStatement st = null;
		ResultSet rs=null;
		try {
			
			st = conn.prepareStatement(" INSERT INTO seller " +
					" (Name, Email, BirthDate, BaseSalary, DepartmentId) " +
					" VALUES " +
					"(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, s.getName());
			st.setString(2, s.getEmail());
			st.setDate(3, new java.sql.Date(s.getBirthDate().getTime()));
			st.setDouble(4, s.getBaseSalary());
			st.setInt(5, s.getDepartment().getId());
			
			Integer rowsAffected = st.executeUpdate();
			
			if(rowsAffected >0) {
				rs = st.getGeneratedKeys();
				if(rs.next()) {
					Integer id = rs.getInt(1);
					s.setId(id);
				}
			}else {
				throw new DbException("Unexpected error! No rows affected");
			}
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		

	}

	@Override
	public void update(Seller s) {
		PreparedStatement st = null;
		try {
			
			st = conn.prepareStatement(
					"UPDATE seller "+
					" SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "+
					"WHERE Id = ? ", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, s.getName());
			st.setString(2, s.getEmail());
			st.setDate(3, new java.sql.Date(s.getBirthDate().getTime()));
			st.setDouble(4, s.getBaseSalary());
			st.setInt(5, s.getDepartment().getId());
			st.setInt(6, s.getId());

			
			Integer rowsAffected = st.executeUpdate();
			if(rowsAffected >0) {
				
			}else {
				throw new DbException("Unexpected error! No rows affected");
			}
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					" DELETE FROM seller WHERE id = ?"
					);
			st.setInt(1, id);
			st.executeUpdate();
		}catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}

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
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
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
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT seller.*,department.Name as DepName " +
					" FROM seller INNER JOIN department "+
					" ON seller.DepartmentId = department.Id "+
					" ORDER BY Name ");
			rs = st.executeQuery();
			
			List<Seller> sList = new ArrayList<>();
			Map<Integer, Department> map  = new HashMap();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId")); // verifica se ja n existe um departamento
				if(dep == null) {// se n exister adicionaro  novo departamento
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"),dep);
				}
				
				Seller s = instantiateSeller(rs,dep);
				sList.add(s);
			}
			return sList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department d) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT seller.*,department.Name as DepName " +
					" FROM seller INNER JOIN department "+
					" ON seller.DepartmentId = department.Id "+
					" WHERE DepartmentId = ? "+
					" ORDER BY Name ");
			st.setInt(1, d.getId());
			rs = st.executeQuery();
			
			List<Seller> sList = new ArrayList<>();
			Map<Integer, Department> map  = new HashMap();
			while(rs.next()) {
				Department dep = map.get(rs.getInt("DepartmentId")); // verifica se ja n existe um departamento
				if(dep == null) {// se n exister adicionaro  novo departamento
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"),dep);
				}
				
				Seller s = instantiateSeller(rs,dep);
				sList.add(s);
			}
			return sList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}
