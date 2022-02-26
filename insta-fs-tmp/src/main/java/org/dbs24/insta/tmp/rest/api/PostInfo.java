/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.tmp.rest.api;

import lombok.Data;

@Data        
public class PostInfo {

    private Long postId;
    private Long actualDate;
    private Long accountId;
    private Long instaPostId;
    private Integer postStatusId;
    private Integer postTypeId;
    private Long mediaId;
    private String shortCode;

}
