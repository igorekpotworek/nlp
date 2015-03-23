package pl.edu.agh.nlp.spark;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModelFilesManager {

	public void saveModel(Object model, String file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(model);
		}
	}

	public Object loadModel(String file) throws IOException, ClassNotFoundException {
		FileInputStream fos = new FileInputStream(file);
		try (ObjectInputStream oos = new ObjectInputStream(fos)) {
			return oos.readObject();
		}
	}
}
