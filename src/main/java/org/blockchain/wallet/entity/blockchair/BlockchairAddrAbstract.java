/**
 * Copyright 2019 bejson.com
 */
package org.blockchain.wallet.entity.blockchair;
import java.util.List;

/**
 * Auto-generated: 2019-05-29 9:41:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BlockchairAddrAbstract {

    private BlockchairAddr address;
    private List<BlockchairAddrTx> transactions;

    public void setAddress(BlockchairAddr address) {
        this.address = address;
    }
    public BlockchairAddr getAddress() {
        return address;
    }

    public List<BlockchairAddrTx> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BlockchairAddrTx> transactions) {
        this.transactions = transactions;
    }
}