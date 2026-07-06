package br.com.uel.featurestore.service;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.uel.featurestore.dao.VersionDAO;
import br.com.uel.featurestore.model.Version;

@Service
public class VersionService {
    private final FeatureService featureService;
    private final VersionDAO versionDAO;

    public VersionService(VersionDAO versionDAO, FeatureService featureService) {
        this.versionDAO = versionDAO;
        this.featureService = featureService;
    }

    public Version createNewVersion(Version versionData, MultipartFile archivePath) {
        if (versionData.getDatasetId() < 0) {
            throw new IllegalArgumentException("Identificador de dataset inválido!");
        }
    
        if (archivePath == null || archivePath.isEmpty()) {
            throw new IllegalArgumentException("Arquivo faltante!");
        }

        try {
            String pastaDestino = System.getProperty("user.dir") + java.io.File.separator + "uploads";
            Path caminhoDiretorio = Paths.get(pastaDestino);

            if (!Files.exists(caminhoDiretorio)) {
                Files.createDirectories(caminhoDiretorio);
            }
            
            
            String nomeOriginal = archivePath.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            String nomeSeguroUnico = UUID.randomUUID().toString() + extensao;

            Path destinoFisicoCompleto = caminhoDiretorio.resolve(nomeSeguroUnico);
            Files.copy(archivePath.getInputStream(), destinoFisicoCompleto, StandardCopyOption.REPLACE_EXISTING);

            BigInteger archiveSize = BigInteger.valueOf(Files.size(destinoFisicoCompleto));
            versionData.setArchivePath(destinoFisicoCompleto.toString());
            versionData.setSize(archiveSize);

        } catch (Exception e) {
            throw new RuntimeException("Falha crítica interna ao gravar o arquivo físico no storage local", e);
        }

        Version rootVersion = versionDAO.getRootVersion(versionData.getDatasetId());

        if (rootVersion == null) {
            versionData.setNumVersion("1.0");
        } else {
            Version lastVersion = versionDAO.getLastVersion(versionData.getDatasetId());

            String[] numVersionBase = lastVersion.getNumVersion().split("\\.");
            String numVersionMajor = numVersionBase[0];
            String numVersionMinor = numVersionBase[1];

            int newNumVersionMinor = Integer.parseInt(numVersionMinor) + 1;
            String newNumVersionBase = numVersionMajor + "." + newNumVersionMinor;
            versionData.setNumVersion(newNumVersionBase);
            versionData.setBaseVersionId(lastVersion.getId());
        }
        versionDAO.createVersion(versionData);
        Version newVersion = versionDAO.getLastVersion(versionData.getDatasetId());
        featureService.extractAndSaveFeaturesFromCSV(newVersion.getId(), newVersion.getArchivePath());
        return newVersion;
    }

    public Version getRootVersion(int datasetId) {
        if (datasetId < 0) {
            throw new IllegalArgumentException("O id do dataset é inválido!");
        }

        Version rootVersion = versionDAO.getRootVersion(datasetId);

        if (rootVersion == null) {
            throw new NoSuchElementException("Nenhuma versão encontrada!");
        }

        return rootVersion;
    }

    public Version getLastVersion(int datasetId) {
        if (datasetId < 0) {
            throw new IllegalArgumentException("O id do dataset é inválido!");
        }

        Version lastVersion = versionDAO.getLastVersion(datasetId);

        if (lastVersion == null) {
            throw new NoSuchElementException("Nenhuma versão encontrada!");
        }

        return lastVersion;
    }

    public List<Version> getListVersion(int datasetId) {
        if (datasetId < 0) {
            throw new IllegalArgumentException("O id do dataset é inválido!");
        }

        List<Version> listVersion = versionDAO.getVersionByDataset(datasetId);

        if (listVersion == null) {
            throw new NoSuchElementException("Nenhuma versão encontrada!");
        }

        return listVersion;
    }

    public void deleteVersion(int versionID) {
        if (versionID < 0) {
            throw new IllegalArgumentException("Id da versão inválido!");
        }

        versionDAO.deleteVersion(versionID);
    }

    public Version getVersionById(int versionID) {
        if (versionID < 0) {
            throw new IllegalArgumentException("Id da versão inválido!");
        } 

        Version version = versionDAO.getVersionById(versionID);
        if (version == null) {
            throw new NoSuchElementException("Nenhuma versão encontrada!");
        }

        return version;
    }
}
