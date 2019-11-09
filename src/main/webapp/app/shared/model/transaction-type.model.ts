export interface ITransactionType {
  id?: number;
  transactionypeCode?: string;
  transactionType?: string;
}

export class TransactionType implements ITransactionType {
  constructor(public id?: number, public transactionypeCode?: string, public transactionType?: string) {}
}
