package org.blockchain.wallet.service;

import org.blockchain.wallet.entity.Concept;

import java.util.List;

public interface ConceptService {

    int insertConcept(Concept concept);

    List<Concept> selectBySelective(Concept concept);
}
