package org.blockchain.wallet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.blockchain.wallet.entity.TxHistory;

import java.util.List;

@Mapper
public interface TxHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TxHistory record);

    int insertSelective(TxHistory record);

    TxHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxHistory record);

    int updateByPrimaryKeyWithBLOBs(TxHistory record);

    int updateByPrimaryKey(TxHistory record);

    int selectCount();

    int deleteAll();

    int deleteByCreateTime();

    List<TxHistory> selectBySelective(TxHistory record);
}