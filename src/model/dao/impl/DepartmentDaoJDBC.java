package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department d) {
			PreparedStatement st = null;
			ResultSet rs=null;
			try {
				
				st = conn.prepareStatement(" INSERT INTO department " +
						" (Name) " +
						" VALUES " +
						"(?) ", Statement.RETURN_GENERATED_KEYS);
				st.setString(1, d.getName());
				
				
				Integer rowsAffected = st.executeUpdate();
				
				if(rowsAffected >0) {
					rs = st.getGeneratedKeys();
					if(rs.next()) {
						Integer id = rs.getInt(1);
						d.setId(id);
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
	public void update(Department d) {
			PreparedStatement st = null;
			ResultSet rs=null;
			try {
				
				st = conn.prepareStatement(" UPDATE department " +
						" SET Name = ?" +
						" WHERE Id = ? " , Statement.RETURN_GENERATED_KEYS);
				st.setString(1, d.getName());
				st.setInt(2, d.getId());

								
				Integer rowsAffected = st.executeUpdate();
				
				
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
					" DELETE FROM department WHERE id = ?"
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
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT * FROM department WHERE Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department d= new Department();
				d.setId(rs.getInt("Id"));
				d.setName(rs.getString("Name"));
				return d;
				
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					" SELECT * FROM department " +
					" ORDER BY Name ");
			rs = st.executeQuery();
			
			List<Department> dList = new ArrayList<>();
			while(rs.next()) {
				Department d= new Department();
				d.setId(rs.getInt("Id"));
				d.setName(rs.getString("Name"));
				dList.add(d);
			}
			return dList;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	

}
