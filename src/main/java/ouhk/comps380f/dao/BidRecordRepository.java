/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ouhk.comps380f.model.BidRecord;

/**
 *
 * @author noctis
 */
public interface BidRecordRepository extends JpaRepository<BidRecord, Long> {
    public BidRecord findByItemId(long ticketId);
}
