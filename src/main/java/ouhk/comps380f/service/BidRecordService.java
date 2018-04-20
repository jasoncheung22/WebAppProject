/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ouhk.comps380f.service;

import ouhk.comps380f.model.BidRecord;

/**
 *
 * @author noctis
 */
public interface BidRecordService {
    public BidRecord getBidRecord(long ticketId);
}
