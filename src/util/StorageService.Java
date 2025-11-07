package util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Produto;

public class StorageService {

    private static final String FILE_PATH = "data/estoque.dat";

    public static void salvar(List<Produto> produtos) {
        try {
            File pasta = new File("data");
            if (!pasta.exists()) pasta.mkdir();

            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            out.writeObject(produtos);
            out.close();
            System.out.println("[LOG] Estoque salvo em memória local.");
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao salvar estoque: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Produto> carregar() {
        try {
            File arquivo = new File(FILE_PATH);
            if (!arquivo.exists()) return new ArrayList<>();

            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH));
            List<Produto> produtos = (List<Produto>) in.readObject();
            in.close();
            System.out.println("[LOG] Estoque carregado da memória local.");
            return produtos;
        } catch (Exception e) {
            System.err.println("[ERRO] Falha ao carregar estoque: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
