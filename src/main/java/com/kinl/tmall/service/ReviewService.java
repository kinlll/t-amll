package com.kinl.tmall.service;

import com.kinl.tmall.VO.ReviewVO;
import com.kinl.tmall.pojo.Review;

import java.util.List;

public interface ReviewService {

    List<ReviewVO> findByPid(Integer pid);

    Integer findCountBypid(Integer pid);

    Integer create(Review review);
}
