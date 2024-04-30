package com.dap.system.office.mapper;

import java.util.List;
import com.dap.system.office.domain.TScore;
import com.dap.system.office.domain.vo.TStudentDetailScoreVo;
import com.dap.system.office.domain.vo.TStudentScoreInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * 分数Mapper接口
 * 
 * @author dap
 * @date 2021-11-12
 */
public interface TScoreMapper 
{
    /**
     * 查询分数
     * 
     * @param id 分数ID
     * @return 分数
     */
    public TStudentDetailScoreVo selectTScoreById(TScore tScore);

    /**
     * 查询分数列表
     * 
     * @param tScore 分数
     * @param courseIds 查询的课程id范围如果为null,则不作限制
     * @return 分数集合
     */
    public List<TScore> selectTScoreList(@Param("score") TScore tScore, @Param("courseIds") List<Integer> courseIds);

    /**
     * 新增分数
     * 
     * @param tScore 分数
     * @return 结果
     */
    public int insertTScore(TScore tScore);



    /**
     * 插入分数前判断是否已经存在
     */
    public Integer existsCourseScore(TScore tScore);

    /**
     * 修改分数
     * 
     * @param tScore 分数
     * @return 结果
     */
    public int updateTScore(TScore tScore);

    /**
     * 删除分数
     * 
     * @param id 分数ID
     * @return 结果
     */
    public int deleteTScoreById(Integer id);

    /**
     * 批量删除分数
     * 
     * @param tScore 需要删除的数据ID
     * @return 结果
     */
    public int deleteTScoreByStuId(TScore tScore);

    List<TStudentScoreInfoVo> selectStudentScoreInfo(@Param("score") TScore tScore, @Param("courseIds") List<Integer> courseIds);

    /**
     * 查询分数的详细信息
     * @param tScore 带查询的条件对象,学生id,学期id
     * @param courseIds 需要查询的课程id
     * @return
     */
    TStudentDetailScoreVo selectScoreDetail(TScore tScore,List<Integer> courseIds);

    Integer existsStudentIds(Integer[] ids);
}
