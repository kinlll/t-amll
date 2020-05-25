package com.kinl.tmall.Utils;

import com.kinl.tmall.VO.ResultVO;
import com.kinl.tmall.enums.ResultEnum;

public class ResultVOUtil {

    public static ResultVO success(Object object){
        ResultVO resultVO = new ResultVO();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMessage("成功");
        return resultVO;
    }

    public static ResultVO success(){
        return success(null);

    }

    public static ResultVO error(Integer code, String msg){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(code);
        resultVO.setMessage(msg);
        return resultVO;
    }

    public static ResultVO error(ResultEnum resultEnum){
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(resultEnum.getCode());
        resultVO.setMessage(resultEnum.getMessage());
        return resultVO;
    }

}
