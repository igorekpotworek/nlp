package pl.edu.agh.nlp.model.entities;

import lombok.Builder;
import lombok.Value;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Value
@Builder
public class Article implements Serializable {

	private String title;
	private String intro;
	private String text;
	private Integer id;
	private Category category;

	public enum Category implements Serializable {

		POLITICS(0), TECH(1), SPORT(2), HEALTH(3);

		private static final Map<Integer, Category> intToTypeMap = new HashMap<>();

		private final int value;

		Category(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		static {
			for (Category type : Category.values()) {
				intToTypeMap.put(type.value, type);
			}
		}

		public static Category fromInt(int i) {
			return intToTypeMap.get(i);
		}

	}

}
