package br.com.uel.featurestore.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import br.com.uel.featurestore.dao.DatasetDao;
import br.com.uel.featurestore.model.Dataset;
import br.com.uel.featurestore.model.User;

@Service
public class DatasetService {
    private final DatasetDao datasetDao;
    private final UserService userService;

    public DatasetService(DatasetDao datasetDao, UserService userService) {
        this.datasetDao = datasetDao;
        this.userService = userService;
    }

    public void inserirDatasetBanco(Dataset dataset) {
        if (dataset.getName() == null || dataset.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O campo nome não pode estar vazio!");
        }
        
        if (dataset.getUserCPF() == null || dataset.getUserCPF().trim().isEmpty()) {
            throw new IllegalArgumentException("O dataset precisa estar vinculado à pelo menos um usuário!");
        }

        datasetDao.saveDataset(dataset);
    }

    public List<Dataset> listDatasets(String userEmail) {
        if (userEmail == null && userEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("O email não pode estar vazio!");
        }
        
        User user = userService.getUserByEmail(userEmail);

        if (user == null) {
            throw new NoSuchElementException("Não foi encontrado um usuário com esse email!");
        }
        
        List<Dataset> datasets = datasetDao.getDataset(user.getCpf(), true);
        return datasets;
    }

    public void desactiveDataset(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Id do dataset necessário para exclusão");
        }

        datasetDao.desactiveDataset(id, false);
    }

    public void updateDatset(Dataset dataset) {
        if (dataset == null) {
            throw new IllegalArgumentException("O objeto dataset não pode estar vazio!");
        }

        if (dataset.getId() <= 0) {
            throw new IllegalArgumentException("O id não pode ser inválido!");    
        }

        if (dataset.getName() == null || dataset.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar vazio!");    
        }

        if (dataset.getDesc() == null || dataset.getDesc().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode estar vazia!");
        }

        datasetDao.updateDataset(dataset);
    }
}
