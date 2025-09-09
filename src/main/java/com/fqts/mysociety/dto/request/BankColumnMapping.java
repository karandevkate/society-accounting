package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BankColumnMapping {

  private int transactionDateIndex;
  private int valueDateIndex;
  private int descriptionIndex;
  private int chequeNumberIndex;
  private int debitAmountIndex;
  private int creditAmountIndex;
  private int balanceIndex;
}

