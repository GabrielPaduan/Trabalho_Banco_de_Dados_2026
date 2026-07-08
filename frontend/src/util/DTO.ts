export interface User {
    cpf: string,
    name: string,
    email: string,
    password: string,
    createdDate: Date
};

export interface Dataset {
    id: number,
    name: string,
    description: string,
    createdDate: Date,
    userCPF: string,
    active: boolean
}

export type DatasetPost = Omit<Dataset, 'id' | 'createdDate' | 'active'>

export interface DataFont {
    id: number,
    name: string
}

export type DataFontPost = Omit<DataFont, 'id'>

export interface DataFontDataset {
    id: number,
    datasetId: number,
    dataFontId: number
}

export type DataFontDatasetPost = Omit<DataFontDataset, 'id'>

export interface Version {
    id: number,
    archivePath: String,
    numVersion: String,
    createdDate: Date,
    datasetId: number,
    baseVersionId: number,
    size: number
}

export interface Feature {
    id: number,
    name: String,
    dataType: String,
    description: String,
    versionId: number
}

export type FeaturePost = Omit<Feature, 'id'>

export interface AccessLog {
    id: number,
    operationType: number,
    dateTime: Date,
    userCPF: String,
    datasetID: number
}

export type AccessLogPost = Omit<AccessLog, 'id'>

export interface GenericGraphData {
    name: String, // dataset name or month
    access: number,
    downloads: number
}

export type GenericGraphDataDatasetRank = Omit<GenericGraphData, 'access'>