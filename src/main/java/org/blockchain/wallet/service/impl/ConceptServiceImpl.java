package org.blockchain.wallet.service.impl;

import org.blockchain.wallet.entity.Concept;
import org.blockchain.wallet.mapper.ConceptMapper;
import org.blockchain.wallet.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ConceptServiceImpl implements ConceptService {

    @Autowired
    ConceptMapper conceptMapper;

    @Override
    public int insertConcept(Concept concept) {

        concept.setCreateTime(new Date());
        return conceptMapper.insert(concept);
    }

    @Override
    public List<Concept> selectBySelective(Concept concept) {
        return conceptMapper.selectBySelective(concept);
    }
}
