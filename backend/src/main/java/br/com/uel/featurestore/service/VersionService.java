package br.com.uel.featurestore.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.VersionDAO;
import br.com.uel.featurestore.model.Version;

@Service
public class VersionService {
    private final VersionDAO versionDAO;

    public VersionService(VersionDAO versionDAO) {
        this.versionDAO = versionDAO;
    }

    public Version createNewVersion(Version versionData) {
        if (versionData.getArchivePath() == null || versionData.getArchivePath().trim().isEmpty()) {
            throw new IllegalArgumentException("É necessário anexar um arquivo!");
        }

        if (versionData.getBaseVersionId() < 0) {
            throw new IllegalArgumentException("Id de versão base inválido!");
        }

        if (versionData.getDatasetId() < 0) {
            throw new IllegalArgumentException("Id do dataset inválido!");
        }

        Version rootVersion = this.getRootVersion(versionData.getDatasetId());

        if (rootVersion == null) {
            versionData.setNumVersion("1.0");
            versionDAO.createVersion(versionData);
            Version newVersion = versionDAO.getLastVersion(versionData.getDatasetId());
            return newVersion;
        }

        // Desenvolver para versões filhas de outras versões
        Version lastVersion = this.getLastVersion(versionData.getDatasetId());
        versionData.setBaseVersionId(lastVersion.getId());

        String[] numVersionBase = lastVersion.getNumVersion().split("\\.");
        String numVersionMajor = numVersionBase[0];
        String numVersionMinor = numVersionBase[1];

        int newNumVersionMinor = Integer.parseInt(numVersionMinor) + 1;

        String newNumVersionBase = numVersionMajor + "." + String.valueOf(newNumVersionMinor);
        
        versionData.setNumVersion(newNumVersionBase);

        versionDAO.createVersion(versionData);
        Version newVersion = this.getLastVersion(versionData.getDatasetId());
        
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
}
