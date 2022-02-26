/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest.api;

import lombok.Data;


@Data
public class SourceInfo {

    private Long postId;
    private Long sourceId;
    private Long actualDate;
    private Integer sourceStatusId;
    private byte[] mainFaceBox;
    private String sourceHash;
    private String sourceUrl;
}
