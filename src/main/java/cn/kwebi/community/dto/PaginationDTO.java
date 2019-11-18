package cn.kwebi.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pages = new ArrayList<>();


    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;
        this.page = page;

        showPrevious = page != 1;
        showNext = !totalPage.equals(page);
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }
            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
        showFirstPage = !pages.contains(1);
        showEndPage = !pages.contains(totalPage);
    }
}
