package org.blockchain.wallet.entity.blockchair;

import java.util.Date;

public class BlockchairAddrTx {

    long block_id;
    String hash;
    Date time;
    long balance_change;

    public long getBlock_id() {
        return block_id;
    }

    public void setBlock_id(long block_id) {
        this.block_id = block_id;
    }

    public void setBlock_id(int block_id) {
        this.block_id = block_id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public long getBalance_change() {
        return balance_change;
    }

    public void setBalance_change(long balance_change) {
        this.balance_change = balance_change;
    }
}
