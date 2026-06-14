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