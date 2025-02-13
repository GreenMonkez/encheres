package fr.eni.tp.encheres.dal.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import fr.eni.tp.encheres.bo.Enchère;


public class EnchèreRowMapper implements RowMapper<Enchère>{

	@Override
	public Enchère mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Enchère e = new Enchère();
		e.setDateEnchère(rs.getTimestamp("DATE_ENCHERE").toLocalDateTime());
		return e;
	}
	
	
	
}
