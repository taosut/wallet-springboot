package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.ConceptDetail;
import org.blockchain.wallet.mapper.ConceptDetailMapper;
import org.blockchain.wallet.service.ConceptDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConceptDetailServiceImpl implements ConceptDetailService {

    @Autowired
    ConceptDetailMapper conceptDetailMapper;

    @Override
    public int insertConceptDetail(ConceptDetail conceptDetail) {

        conceptDetail.setCreateTime(new Date());
        return conceptDetailMapper.insert(conceptDetail);
    }
}
