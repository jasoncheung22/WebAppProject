/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import javax.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import ouhk.comps380f.dao.BidRecordRepository;
import ouhk.comps380f.model.BidRecord;

/**
 *
 * @author noctis
 */
public class BidRecordServiceImpl implements BidRecordService {
    @Resource
    private BidRecordRepository bidRecordRepo;
    
    @Override
    @Transactional
    public BidRecord getBidRecord(long itemId) {
        return bidRecordRepo.findByItemId(itemId);
    }
}
