package br.com.uel.featurestore.service;

import org.springframework.stereotype.Service;
import br.com.uel.featurestore.dao.FeatureDAO;
import br.com.uel.featurestore.model.Feature;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FeatureService {

    private final FeatureDAO featureDAO;

    // Injeção de dependência via Construtor (Recomendado)
    public FeatureService(FeatureDAO featureDAO) {
        this.featureDAO = featureDAO;
    }

    public Feature createFeature(Feature featureData) {
        if (featureData.getName() == null || featureData.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da feature não pode ser vazio!");
        }
        Feature feature = featureDAO.insertNewFeature(featureData);
        System.out.println("Retorno service: " + feature);
        return feature;
    }

    public void extractAndSaveFeaturesFromCSV(int idVersao, String arquivoPath) {
        if (arquivoPath == null || arquivoPath.trim().isEmpty()) {
            return;
        }

        Path path = Paths.get(arquivoPath);
        if (!Files.exists(path)) {
            System.err.println("Arquivo não encontrado no disco para extração de features: " + arquivoPath);
            return;
        }

        if (arquivoPath.endsWith(".csv") || arquivoPath.endsWith(".txt")) {
            try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
                String firstLine = br.readLine();
                
                if (firstLine != null && !firstLine.trim().isEmpty()) {
                    String[] columns = firstLine.split("[,;]");
                    
                    for (String columnsName : columns) {
                        String name = columnsName.trim().replace("\"", "");
                        
                        if (!name.isEmpty()) {
                            Feature newFeature = new Feature();
                            newFeature.setName(name);
                            newFeature.setDataType("TEXT");
                            newFeature.setDescription("Feature extraída automaticamente do cabeçalho do arquivo.");
                            
                            newFeature.setVersionId(idVersao); 
                            
                            this.createFeature(newFeature);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Falha ao ler o cabeçalho do arquivo para extrair as features: " + e.getMessage());
            }
        }
    }

    public List<Feature> getFeaturesByVersionId(int versionId) {
        if (versionId < 0) {
            throw new IllegalArgumentException("O ID da versão é inválido!");
        }
        
        List<Feature> features = featureDAO.getFeaturesByVersionId(versionId);
        
        return features;
    }

    public Feature getFeatureById(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O ID da feature é inválido!");
        }
        
        Feature feature = featureDAO.getFeatureById(id);
        if (feature == null) {
            throw new NoSuchElementException("Nenhuma feature encontrada com este ID!");
        }
        
        return feature;
    }

    public void updateFeature(int id, Feature featureData) {
        if (id < 0) {
            throw new IllegalArgumentException("O ID da feature é inválido!");
        }
        if (featureData.getName() == null || featureData.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da feature não pode ser vazio!");
        }

        Feature existingFeature = featureDAO.getFeatureById(id);
        if (existingFeature == null) {
            throw new NoSuchElementException("Feature não encontrada para atualização!");
        }
        
        featureDAO.updateFeature(id, featureData);
    }

    public void deleteFeature(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O ID da feature é inválido!");
        }
        
        Feature existingFeature = featureDAO.getFeatureById(id);
        if (existingFeature == null) {
            throw new NoSuchElementException("Feature não encontrada para exclusão!");
        }

        featureDAO.deleteFeature(id);
    }
}