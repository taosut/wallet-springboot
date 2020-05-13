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
    private List<String> transactions;

    public void setAddress(BlockchairAddr address) {
        this.address = address;
    }
    public BlockchairAddr getAddress() {
        return address;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }
    public List<String> getTransactions() {
        return transactions;
    }

}