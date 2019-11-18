package cn.kwebi.community.dto;

import lombok.Data;

@Data
public class AccessTockenDTO {
    private String client_id, client_secret,code,redirect_uri,state;
}
