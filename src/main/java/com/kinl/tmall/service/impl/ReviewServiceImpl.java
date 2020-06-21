package com.kinl.tmall.service.impl;

import com.kinl.tmall.VO.ReviewVO;
import com.kinl.tmall.VO.UserVO;
import com.kinl.tmall.dao.ReviewMapper;
import com.kinl.tmall.pojo.Review;
import com.kinl.tmall.pojo.ReviewExample;
import com.kinl.tmall.service.ReviewService;
import com.kinl.tmall.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private UserService userService;

    @Override
    public List<ReviewVO> findByPid(Integer pid) {
        ReviewExample reviewExample = new ReviewExample();
        ReviewExample.Criteria criteria = reviewExample.createCriteria();
        criteria.andPidEqualTo(pid);
        List<Review> reviews = reviewMapper.selectByExample(reviewExample);
        List<ReviewVO> reviewVOS = new ArrayList<>();
        for (Review review : reviews) {
            UserVO userVO = userService.findVOById(review.getUid());
            ReviewVO reviewVO = new ReviewVO();
            reviewVO.setContent(review.getContent());
            reviewVO.setCreateDate(review.getCreatedate());
            reviewVO.setUser(userVO);
            reviewVOS.add(reviewVO);
        }
        return reviewVOS;
    }

    @Override
    public Integer findCountBypid(Integer pid) {
        ReviewExample reviewExample = new ReviewExample();
        ReviewExample.Criteria criteria = reviewExample.createCriteria();
        criteria.andPidEqualTo(pid);
        int count = reviewMapper.countByExample(reviewExample);
        return count;
    }
}
