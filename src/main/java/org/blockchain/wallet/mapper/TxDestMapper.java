package org.blockchain.wallet.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.blockchain.wallet.entity.TxDest;

import java.util.List;

@Mapper
public interface TxDestMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TxDest record);

    int insertSelective(TxDest record);

    TxDest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TxDest record);

    int updateByPrimaryKey(TxDest record);

    List<TxDest> selectBySelective(TxDest record);

    int deleteBySelective(TxDest record);
}