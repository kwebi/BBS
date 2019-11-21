package cn.kwebi.community.dto;

import lombok.Data;

@Data
public class GithubUser {
    private String name;
    private String login;
    private Long id;
    private String bio;
    private String avatarUrl;
}
