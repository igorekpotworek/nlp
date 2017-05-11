package pl.edu.agh.nlp.model;

import lombok.experimental.UtilityClass;

import java.sql.ResultSet;
import java.sql.SQLException;

@UtilityClass
class ResultSetExtension {

	static Integer getIntOrNull(ResultSet resultSet, String columnLabel) {
		try {
			return resultSet.getInt(columnLabel);
		} catch (SQLException e) {
			return null;
		}
	}

	static String getStringOrNull(ResultSet resultSet, String columnLabel) {
		try {
			return resultSet.getString(columnLabel);
		} catch (SQLException e) {
			return null;
		}
	}

	static Double getDoubleOrNull(ResultSet resultSet, String columnLabel) {
		try {
			return resultSet.getDouble(columnLabel);
		} catch (SQLException e) {
			return null;
		}
	}
}
